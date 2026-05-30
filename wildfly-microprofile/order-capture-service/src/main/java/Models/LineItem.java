package Models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "LINEITEM")
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINENUM")
    private Integer lineNum;

    @Column(name = "ITEMID", nullable = false, length = 10)
    private String itemId;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Column(name = "UNITPRICE", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDERID", nullable = false)
    private Orders order;

    public Integer getLineNum() { return lineNum; }
    public void setLineNum(Integer lineNum) { this.lineNum = lineNum; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Orders getOrder() { return order; }
    public void setOrder(Orders order) { this.order = order; }
}