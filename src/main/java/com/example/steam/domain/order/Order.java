package com.example.steam.domain.order;

import com.example.steam.domain.user.User;
import com.example.steam.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @Column(nullable = true)
    private String tid;

    @Column(nullable = false)
    private int totalPrice; // 총 결제 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.READY;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderItem> orderItems;

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Builder
    public Order(User user, int totalPrice) {
        this.user = user;
        this.totalPrice = totalPrice;
    }

    // 주문 아이템들 추가 메서드
    public void addOrderItems(List<OrderItem> items) {
        this.orderItems.addAll(items);
    }

    // 결제 완료
    public void completeOrder() {
        this.tid = tid;
        this.status = OrderStatus.COMPLETED;
    }

    // 결제 실패
    public void failed() {
        this.status = OrderStatus.FAILED;
    }

    // TID(결제 고유번호) 저장
    public void addTid(String tid) {
        this.tid = tid;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELED;
    }
}
