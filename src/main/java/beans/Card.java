
package beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Card {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pos")
    @Expose
    private Double pos;
    @SerializedName("shortLink")
    @Expose
    private String shortLink;
    @SerializedName("shortUrl")
    @Expose
    private String shortUrl;
    @SerializedName("url")
    @Expose
    private String url;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPos() {
        return pos;
    }

    public String getShortLink() {
        return shortLink;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("pos", pos).append("shortLink", shortLink).append("shortUrl", shortUrl).append("url", url).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pos).append(shortUrl).append(name).append(id).append(shortLink).append(url).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Card) == false) {
            return false;
        }
        Card rhs = ((Card) other);
        return new EqualsBuilder().append(pos, rhs.pos).append(shortUrl, rhs.shortUrl).append(name, rhs.name).append(id, rhs.id).append(shortLink, rhs.shortLink).append(url, rhs.url).isEquals();
    }

}
