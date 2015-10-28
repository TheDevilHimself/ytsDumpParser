import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * Created by the Devil himself on 26/10/2015.
 */
public class LoadThread implements Runnable{
    ArrayList<Film> filmsList;
    DefaultListModel<Film> listModel;

    @Override
    public void run() {
    if(filmsList==null)
        filmsList=new ArrayList<>();

        int index=0;
        try (Stream<String> stream = Files.lines(Paths.get("yts_movie.json"))) {
            for (String line : (Iterable<String>) stream::iterator)
            {
                JSONObject obj = new JSONObject(line);

                JSONArray genres=obj.getJSONArray("genres");
                StringBuffer filmGenres=new StringBuffer();
                for(int i=0;i<genres.length();i++)
                    filmGenres.append(genres.getString(0) + ",");
                filmGenres.deleteCharAt(filmGenres.length()-1);

                JSONArray torrent=obj.getJSONArray("torrents");
                JSONObject torrentInfo=torrent.getJSONObject(0);

                Film f=new Film(obj.getString("imdb_code"),
                        obj.getString("title"),
                        obj.getInt("year"),
                        obj.getDouble("rating"),
                        obj.getInt("runtime"),
                        filmGenres.toString(),
                        obj.getString("language"),
                        obj.getString("mpa_rating"),
                        torrentInfo.getString("hash"),
                        torrentInfo.getString("quality"),
                        torrentInfo.getString("size"),
                        obj.getString("date_uploaded"));
                filmsList.add(f);
                /*Film(String imdbCode, String title, int year, double rating,
                int lenght, String genres, String language, String ageRating, String hash, String quality, String sizeMB, String dateUploaded) {*/

            }
            stream.close();
            Collections.sort(filmsList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Film film:filmsList) {
            SwingUtilities.invokeLater(
                    new Runnable() {
                        public void run() {
                            listModel.addElement(film);
                        }
                    });
        }

    }

    public LoadThread(ArrayList<Film> filmsList, DefaultListModel<Film> listModel) {
        this.filmsList = filmsList;
        this.listModel = listModel;
    }
}
