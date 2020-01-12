
package beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;

public class List {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("closed")
    @Expose
    private Boolean closed;
    @SerializedName("idBoard")
    @Expose
    private String idBoard;
    @SerializedName("pos")
    @Expose
    private Integer pos;
    @SerializedName("cards")
    @Expose
    private java.util.List<Card> cards = new ArrayList<Card>();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getClosed() {
        return closed;
    }

    public String getIdBoard() {
        return idBoard;
    }

    public Integer getPos() {
        return pos;
    }

    public java.util.List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("closed", closed).append("idBoard", idBoard).append("pos", pos).append("cards", cards).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(idBoard).append(cards).append(pos).append(name).append(closed).append(id).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof List) == false) {
            return false;
        }
        List rhs = ((List) other);
        return new EqualsBuilder().append(idBoard, rhs.idBoard).append(cards, rhs.cards).append(pos, rhs.pos).append(name, rhs.name).append(closed, rhs.closed).append(id, rhs.id).isEquals();
    }

}
