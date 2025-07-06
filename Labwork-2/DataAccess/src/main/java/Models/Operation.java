package Models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operation_id_gen")
    @SequenceGenerator(name = "operation_id_gen", sequenceName = "operation_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OperationType type;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_account")
    private BankAccount homeAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_account", nullable = false)
    private BankAccount targetAccount;

    @Column(name = "date", nullable = false)
    private Instant date;

    public Operation(OperationType type,
                     BigDecimal amount,
                     BankAccount homeAccount,
                     BankAccount targetAccount,
                     Instant date) {
        this.type = type;
        this.amount = amount;
        this.homeAccount = homeAccount;
        this.targetAccount = targetAccount;
        this.date = date;
    }

    public Operation() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BankAccount getHomeAccount() {
        return homeAccount;
    }

    public void setHomeAccount(BankAccount homeAccount) {
        this.homeAccount = homeAccount;
    }

    public BankAccount getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(BankAccount targetAccount) {
        this.targetAccount = targetAccount;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

}