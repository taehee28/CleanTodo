# CleanTodo

Clean Architecture를 구현하기 위해 만든 Todo 앱

Compose, Room, Hilt 사용 

## presentation layer
* TodoViewModel
  * domain 레이어의 UseCase를 주입받아 상황에 맞는 UseCase를 호출 
  * local database에서 받아온 Flow인 todo 리스트를 StateFlow로 변경
* MainActivity.kt
  * TodoViewModel과 화면을 구성하는 Composable을 연결
* TodoScreen.kt
  * UI에 해당하는 Composable의 모음
  * TodoViewModel에서 넘겨받은 StateFlow인 todo 리스트를 collectAsState로 구독하여 화면을 자동으로 업데이트할 수 있게 함

## domain layer
* usecase.kt
  * 기능단위로 나누어진 UseCase 클래스를 작성
  * TodoRepository 인터페이스를 주입받음
* repository.kt
  * UseCase에서 사용할 인터페이스
  * 실제 구현은 data 레이어에서 함
* model.kt
  * domain과 presentation 레이어에서 사용할 데이터 모델(Entity)

## data layer
* repository.kt
  * domain 레이어의 TodoRepository 인터페이스를 구현
  * TodoDataSource 인터페이스를 주입받아 사용
  * mapper를 사용해서 데이터 모델을 각각의 레이어에서 쓸 수 있는 데이터 모델로 변환
* datasource.kt
  * local database를 source로 하는 인터페이스와 구현 클래스
  * Room의 Dao를 주입받아 사용함
* model.kt
  * data 레이어에서 사용할 데이터 모델
  * Room에서 해당 모델을 사용해서 테이블 구성 
* mapper.kt
  * domain 레이어의 데이터 모델과 data 레이어의 데이터 모델을 서로로 변환할 수 있는 mapper 정의 
