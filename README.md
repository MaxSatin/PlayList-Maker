## Playlist-maker

Приложение позволяет пользователю прослушивать отрывки любимых песен и создавать из них плейлисты. Приложение способно хранить историю поиска
в офлайн режиме. Для выбора обложек для плейлистов приложение использует внутреннее хранилище пользователя. В приложении
доступен dark mode, возможность делиться плейлистами и информацией о приложении. Прижение поддерживает только вертикальную ориентацию.

В качестве базы аудиозаписей используется сервис iTunes, который позволяет искать по всей публично доступной библиотеке iTunes.
Благодаря этому перед добавлением песни в плейлист пользователь сможет послушать её и убедиться, что нашёл то, что ему нужно.

## Screenshots ## 
<img width="200" height="426" alt="Image" src="https://github.com/user-attachments/assets/d3a679cb-bd16-4dac-92b2-3f7352c366fb" /> <img width="200" height="426" alt="Image" src="https://github.com/user-attachments/assets/31e4334c-0240-4b35-b7b8-35db9208628e" /> 
<img width="200" height="426" alt="Image" src="https://github.com/user-attachments/assets/26040aa3-f42b-464a-bcd6-86ea01f173e3" /> <img width="200" height="426" alt="Image" src="https://github.com/user-attachments/assets/a9c91f86-f3ce-4e3a-89dc-22df22276afc" />
<img width="200" height="426" alt="Image" src="https://github.com/user-attachments/assets/e9f2b243-9608-446d-9421-f847ff629abc" /> <img width="200" height="426" alt="Image" src="https://github.com/user-attachments/assets/db68f725-aea4-42a2-b186-08eb9e275461" />

## Screencasts ## 

![Image](https://github.com/user-attachments/assets/05b81b88-7f37-415c-bad9-b3e46e7e8f37)
![Image](https://github.com/user-attachments/assets/ee9d13ab-b4b1-48b3-8875-278bfabf5188) ![Image](https://github.com/user-attachments/assets/742475a8-2604-4481-82cb-c434d57e1adf)

## Цель проекта
Проект является индивидуальной работой и основным в портфолио. Приложение демонстрирует основные навыки, полученные в ходе проектной работы.

## Стек: 

** Платформа ** 
- Android

** Tech ** 
- Kotlin
- Android SDK 34
- Min SDK 29

** Architecture ** 
- MVVM (ViewModel + LiveData)
- DI: Koin
- Асинхронность: Kotlin Coroutines
- Permissions: Peko

** Data ** 
- Local DB: Room + SharedPreferences
- Net: Retrofit + Gson

** UI & Design **
- ViewBinding + XML
- Material Design Components
- Constraint Layout
- Jetpack Navigation
- Glide for images
- RecyclerView for lists

## Планы и рефакторинг:
- Добавление фичи на MVI + Compose:
  "информация об исполнителе" + "рекомендации"

- Поиск потенциальных утечек памяти и узких мест замедляющих производительность
- Рефакторинг существующего кода для большего соответствия принципам KISS и SOLID
