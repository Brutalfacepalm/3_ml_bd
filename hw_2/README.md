### Блок 1. ###

[Скрин beeline](https://github.com/Brutalfacepalm/3_ml_bd/blob/master/hw_1/Screenshot%20from%202021-10-03%2022-18-06.png?raw=true) \
[Скрин hue](https://github.com/Brutalfacepalm/3_ml_bd/blob/master/hw_1/Screenshot%20from%202021-10-03%2022-17-41.png?raw=true)


### Блок 2. ###
###### Запросы слал в beeline ######
#### Сделать таблицу artists в Hive ####

    CREATE TABLE artists (mbid string, artist_mb string, artist_lastfm string, country_mb string, country_lastfm string, tags_mb string, tags_lastfm string, listeners_lastfm bigint, scrobbles_lastfm bigint, ambiguous_artist boolean) row format delimited fields terminated by ',' tblproperties("skip.header.line.count"="1");
          
    LOAD DATA LOCAL INPATH '/opt/artists.csv' OVERWRITE INTO TABLE artists;

#### Используя Hive найти: ####

###### (a) Исполнителя с максимальным числом скробблов ######
    SELECT artist_mb FROM artists WHERE artists.scrobbles_lastfm IN (SELECT MAX(scrobbles_lastfm) FROM artists);
<details>
    <summary>Output</summary>

    +--------------+
    |  artist_mb   |
    +--------------+
    | The Beatles  |
    +--------------+

</details>

###### (b) Самый популярный тэг на ластфм ######
    SELECT CONCAT_WS('', t.tag) as tag FROM (SELECT SPLIT(tags_lastfm, ";") as tag, COUNT(*) as cnt FROM artists WHERE (tags_lastfm <> '') GROUP BY tags_lastfm SORT BY cnt DESC LIMIT 1) t;
<details>
    <summary>Output</summary>

    +------------+
    |    tag     |
    +------------+
    | seen live  |
    +------------+

</details>

###### (с) Самые популярные исполнители 10 самых популярных текгов ластфм ######
    CREATE TABLE top_listened_tag AS SELECT art.artist_mb, top10.tag, art.listeners_lastfm FROM (SELECT artist_mb, tag, listeners_lastfm FROM artists LATERAL VIEW EXPLODE(SPLIT(tags_lastfm, ';')) tags_lastfm as tag) AS art JOIN (SELECT * FROM tags) AS top10 ON (TRIM(art.tag) = TRIM(top10.tag));
      
    SELECT t.artist_mb as artist, t.tag as tag, t.listeners_lastfm as listeners FROM (SELECT *, ROW_NUMBER() OVER( PARTITION BY tag ORDER BY listeners_lastfm DESC) as rn FROM top_listened_tag) t WHERE rn = 1 ORDER BY listeners DESC LIMIT 10;
<details>
    <summary>Output</summary>

    +----------------------+-----------------------+------------+
    |        artist        |          tag          | listeners  |
    +----------------------+-----------------------+------------+
    | Coldplay             | seen live             | 5381567    |
    | Coldplay             | electronic            | 5381567    |
    | Rihanna              | House                 | 4558193    |
    | The Rolling Stones   | blues                 | 3798330    |
    | Daft Punk            | trance                | 3782404    |
    | Beyonc?              | jazz                  | 3554615    |
    | Johnny Cash          | rockabilly            | 2795136    |
    | Jason Derulo         | All                   | 1872933    |
    | Jeremih              | spotify               | 966028     |
    | Diddy - Dirty Money  | under 2000 listeners  | 503188     |
    +----------------------+-----------------------+------------+

</details>


###### (d) Любой другой инсайт: Самая популярная группа из страны, представленной наибольшим числов групп на ластфм ######
    SELECT artist_mb as artist, listeners_lastfm as listeners, country_lastfm as country FROM artists WHERE country_lastfm IN (SELECT country_lastfm FROM (SELECT country_lastfm, COUNT(*) as cnt FROM artists WHERE country_lastfm <> '' GROUP BY country_lastfm SORT BY cnt DESC LIMIT 1) t) ORDER BY listeners DESC LIMIT 1;
<details>
    <summary>Output</summary>

    +------------------------+------------+----------------+
    |         artist         | listeners  |    country     |
    +------------------------+------------+----------------+
    | Red Hot Chili Peppers  | 4620835    | United States  |
    +------------------------+------------+----------------+
</details>