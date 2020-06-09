# 2_RoomNote

실시간 채팅을 지원하는 간단한 채팅앱
- 추가기능으로 모든 채팅방은 일정 시간이 지나면 자동으로 삭제 
- android(java), express.js, postrgresql 사용
- Http 통신과 socket통신을 병행하여 사용

이전 프로젝트인 HanChat에서 부족하다고 느낀 부분을 개선하는 것에 집중했다.
- RecyclerView Adapter를 완전히 독립적으로 구분하였다.  
여기에 들어가는 아이템은 이제 자신의 레이아웃을 자신이 지정할 수 있다.
- ViewModel을 활용하여 동작을 명확히 구분했다.

샘플 영상: sample.mp4
