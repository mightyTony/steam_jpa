# 프로젝트 스팀
게임 관련 커뮤니티와 편리하게 게임을 구매 할 수 있는 퍼블리싱 서비스 입니다. 

## 기술 스택 
Java, Spring, Spring Data JPA, Spring Security, QueryDSL, JWT, MySQL, Docker, Redis, AWS, Github Actions

## ERD 구조
![diagram](https://github.com/user-attachments/assets/9d8812f7-a788-4f7c-a4f3-63fcda3f95f2)


## API 명세

| Domain   | URL                                                        | HTTP Method | Description                     | 접근 권한 | 토큰 필요 |
|----------|------------------------------------------------------------|-------------|----------------------------------|------------|-------------|
| Auth     | /api/v1/auth/signup                                        | POST        | 사용자 회원가입                 | -          | X           |
| Auth     | /api/v1/auth/signup-admin                                  | POST        | 관리자 회원가입                 | -          | X           |
| Auth     | /api/v1/auth/login                                         | POST        | 로그인                          | -          | X           |
| Auth     | /api/v1/auth                                               | DELETE      | 회원 탈퇴                       | USER       | O           |
| Cart     | /api/v1/cart                                               | GET         | 장바구니 목록 조회              | USER       | O           |
| Cart     | /api/v1/cart                                               | POST        | 장바구니에 게임 추가            | USER       | O           |
| Cart     | /api/v1/cart/{game}                                        | DELETE      | 장바구니에서 게임 삭제          | USER       | O           |
| Review   | /api/v1/game/{gameId}/review                               | GET         | 리뷰 목록 조회                  | -          | X           |
| Review   | /api/v1/game/{gameId}/review                               | POST        | 리뷰 작성                       | USER       | O           |
| Review   | /api/v1/game/{gameId}/review/{reviewId}                   | PATCH       | 리뷰 수정                       | USER       | O           |
| Review   | /api/v1/review/{reviewId}                                  | DELETE      | 리뷰 삭제                       | USER       | O           |
| Review   | /api/v1/game/{gameId}/{reviewId}/like                     | POST        | 리뷰 좋아요/취소                | USER       | O           |
| Payment  | /api/v1/payment/ready                                      | POST        | 장바구니 전체 결제 요청         | USER       | O           |
| Payment  | /api/v1/payment/ready/selected                             | POST        | 선택 게임 결제 요청             | USER       | O           |
| Payment  | /api/v1/payment/ready/now                                  | POST        | 즉시 결제 요청                  | USER       | O           |
| Payment  | /api/v1/payment/success                                    | GET         | 결제 승인                       | USER       | O           |
| Payment  | /api/v1/payment/history                                    | GET         | 결제 내역 조회                  | USER       | O           |
| Payment  | /api/v1/payment/fail                                       | GET         | 결제 실패                       | USER       | O           |
| Payment  | /api/v1/payment/cancel                                     | GET         | 결제 취소                       | USER       | O           |
| Profile  | /api/v1/profile/user/{userId}/edit/profile/image           | PUT         | 프로필 이미지 수정              | USER       | O           |
| Profile  | /api/v1/profile/user/{userId}/edit/profile/info            | PATCH       | 프로필 정보 수정                | USER       | O           |
| Profile  | /api/v1/profile/user                                       | GET         | 내 프로필 조회                  | USER       | O           |
| Profile  | /api/v1/profile/user/{userId}/game                         | GET         | 보유 게임 조회                  | USER       | O           |
| Profile  | /api/v1/profile/{profileId}/comment                        | GET         | 댓글 목록 조회                  | -          | X           |
| Profile  | /api/v1/profile/{profileId}/comment                        | POST        | 댓글 작성                       | USER       | O           |
| Profile  | /api/v1/profile/{profileId}/comment/{commentId}           | DELETE      | 댓글 삭제                       | USER       | O           |
| Friend   | /api/v1/friendship/request                                 | POST        | 친구 신청                       | USER       | O           |
| Friend   | /api/v1/friendship/request/{requestId}                    | DELETE      | 친구 요청 취소                  | USER       | O           |
| Friend   | /api/v1/friendship/request/{requestId}                    | PATCH       | 친구 요청 수락/거절            | USER       | O           |
| Friend   | /api/v1/friendship/request/search                          | GET         | 내가 보낸 친구 요청 조회       | USER       | O           |
| Friend   | /api/v1/friendship/request/received                        | GET         | 받은 친구 요청 목록             | USER       | O           |
| Friend   | /api/v1/friendship/friends                                 | GET         | 내 친구 목록 조회               | USER       | O           |
| Friend   | /api/v1/friendship/friends-short                           | GET         | 프로필용 친구 일부 조회        | USER       | O           |
| Friend   | /api/v1/friendship/friendship/{friendshipId}              | DELETE      | 친구 삭제                       | USER       | O           |
| Game     | /api/v1/game                                               | POST        | 게임 등록                       | ADMIN      | O           |
| Game     | /api/v1/game/{gameId}/edit/image                           | PUT         | 게임 이미지 수정                | ADMIN      | O           |
| Game     | /api/v1/game/{id}/open                                     | PATCH       | 게임 오픈                       | ADMIN      | O           |
| Game     | /api/v1/game/{id}/info                                     | PATCH       | 게임 정보 수정                  | ADMIN      | O           |
| Game     | /api/v1/game/{id}/discount                                 | PATCH       | 게임 할인 적용                  | ADMIN      | O           |
| Game     | /api/v1/game/{id}                                          | GET         | 게임 상세 조회                  | -          | X           |
| Game     | /api/v1/game/{id}                                          | DELETE      | 게임 삭제                       | ADMIN      | O           |
| Game     | /api/v1/game/weekly                                        | GET         | 주간 게임 판매 랭킹             | -          | X           |
| Game     | /api/v1/game/monthly                                       | GET         | 월간 게임 판매 랭킹             | -          | X           |
| Game     | /api/v1/game/category                                      | GET         | 게임 목록 조회                  | -          | X           |
| Notification | /api/v1/notifications/subscribe        | GET         | 알림 서비스 구독             | USER       | O          |
| Notification | /api/v1/notifications/latest           | GET         | 최근 받은 알림 10개를 조회   | USER       | O          |
| Notification | /api/v1/notifications/count            | GET         | 안 읽은 알림 개수 조회       | USER       | O          |
| Wish   | /api/v1/wish                     | POST        | 상품을 찜 목록에 등록합니다.     | USER       | O          |
| Wish   | /api/v1/wish                     | DELETE      | 해당 상품을 찜 목록에서 삭제합니다. | USER       | O          |
| Wish   | /api/v1/wish/move/{game}         | PATCH       | 찜한 상품을 장바구니로 옮깁니다.   | USER       | O          |
| Wish   | /api/v1/wish/user/{userId}       | GET         | 찜 목록 조회                      | USER       | O          |
