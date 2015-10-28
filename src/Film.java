/**
 * Created by the Devil himself on 26/10/2015.
 */
public class Film implements Comparable<Film>{

    String imdbCode;
    String title;
    int year;
    double rating;
    int lenght;
    String genres;
    String language;
    String ageRating;
    String hash;
    String quality;
    String sizeMB;
    String dateUploaded;

    public Film(String imdbCode, String title, int year, double rating, int lenght, String genres, String language, String ageRating, String hash, String quality, String sizeMB, String dateUploaded) {
        this.imdbCode = "www.imdb.com/title/"+imdbCode;
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.lenght = lenght;
        this.genres = genres;
        this.language = language;
        this.ageRating = ageRating;
        this.hash = hash+".torrent";
        this.quality = quality;
        this.sizeMB = sizeMB;
        this.dateUploaded = dateUploaded;
    }

    @Override
    public String toString() {
        return title + " ("+year+")";
    }

    public int compareTo(Film f)
    {
        if(f!=null)
        {
            return getTitle().compareTo(f.getTitle());
        }
        return 1;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Film))
            return false;
        Film f=(Film) o;
        return f.getHash().equalsIgnoreCase(getHash());
    }

    public String getFormattedInformations()
    {
        StringBuffer info=new StringBuffer();
        info.append("Name : ");
        info.append(title+"\n");
        info.append("imdb : ");
        info.append(imdbCode+"\n");
        info.append("Year : ");
        info.append(year+"\n");
        info.append("Rating : ");
        info.append(rating+"\n");
        info.append("Lenght : ");
        info.append(lenght+"\n");
        info.append("Genres : ");
        info.append(genres+"\n");
        info.append("Language : ");
        info.append(language+"\n");
        info.append("Age Rating : ");
        info.append(ageRating+"\n");
        info.append("Quality : ");
        info.append(quality+"\n");
        info.append("Size (MB) : ");
        info.append(sizeMB+"\n");
        info.append("Upload Date : ");
        info.append(dateUploaded+"\n");
        info.append("Torrent : ");
        info.append(hash+"\n");

        return info.toString();
    }

    public String getImdbCode() {
        return imdbCode;
    }

    public void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSizeMB() {
        return sizeMB;
    }

    public void setSizeMB(String sizeMB) {
        this.sizeMB = sizeMB;
    }

    public String getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }
}
