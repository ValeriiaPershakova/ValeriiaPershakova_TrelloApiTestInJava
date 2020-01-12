
package beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;

public class Board {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("closed")
    @Expose
    private Boolean closed;
    @SerializedName("idOrganization")
    @Expose
    private String idOrganization;
    @SerializedName("pinned")
    @Expose
    private Boolean pinned;
    @SerializedName("starred")
    @Expose
    private Boolean starred;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("shortLink")
    @Expose
    private String shortLink;
    @SerializedName("lists")
    @Expose
    private java.util.List<beans.List> lists = new ArrayList<beans.List>();
    @SerializedName("dateLastActivity")
    @Expose
    private String dateLastActivity;
    @SerializedName("dateLastView")
    @Expose
    private String dateLastView;
    @SerializedName("shortUrl")
    @Expose
    private String shortUrl;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Boolean getClosed() {
        return closed;
    }

    public String getIdOrganization() {
        return idOrganization;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public Boolean getStarred() {
        return starred;
    }

    public String getUrl() {
        return url;
    }

    public String getShortLink() {
        return shortLink;
    }

    public java.util.List<beans.List> getLists() {
        return lists;
    }

    public String getDateLastActivity() {
        return dateLastActivity;
    }

    public String getDateLastView() {
        return dateLastView;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("desc", desc).append("closed", closed).append("idOrganization", idOrganization).append("pinned", pinned).append("starred", starred).append("url", url).append("shortLink", shortLink).append("lists", lists).append("dateLastActivity", dateLastActivity).append("dateLastView", dateLastView).append("shortUrl", shortUrl).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pinned).append(shortUrl).append(dateLastActivity).append(shortLink).append(url).append(starred).append(lists).append(name).append(idOrganization).append(dateLastView).append(closed).append(id).append(desc).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Board) == false) {
            return false;
        }
        Board rhs = ((Board) other);
        return new EqualsBuilder().append(pinned, rhs.pinned).append(shortUrl, rhs.shortUrl).append(dateLastActivity, rhs.dateLastActivity).append(shortLink, rhs.shortLink).append(url, rhs.url).append(starred, rhs.starred).append(lists, rhs.lists).append(name, rhs.name).append(idOrganization, rhs.idOrganization).append(dateLastView, rhs.dateLastView).append(closed, rhs.closed).append(id, rhs.id).append(desc, rhs.desc).isEquals();
    }

}
