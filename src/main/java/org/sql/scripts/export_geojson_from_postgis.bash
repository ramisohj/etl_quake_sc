conda activate env

ogr2ogr -f GeoJSON bol_adm0.geojson PG:"dbname=postgis user=postgres password=postgres" -sql "select * from bol_adm0"
ogr2ogr -f GeoJSON bol_adm1.geojson PG:"dbname=postgis user=postgres password=postgres" -sql "select * from bol_adm1"
ogr2ogr -f GeoJSON bol_adm2.geojson PG:"dbname=postgis user=postgres password=postgres" -sql "select * from bol_adm2"
ogr2ogr -f GeoJSON bol_adm3.geojson PG:"dbname=postgis user=postgres password=postgres" -sql "select * from bol_adm3"

ogr2ogr -f GeoJSON eq_bol.geojson PG:"dbname=postgis user=postgres password=postgres" -sql "select * from eq_bol"