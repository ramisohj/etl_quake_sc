conda activate env

ogr2ogr -f "PostgreSQL" PG:"dbname=postgis user=postgres password=postgres" "bol_adm0.geojson"  -nln bol_map0
ogr2ogr -f "PostgreSQL" PG:"dbname=postgis user=postgres password=postgres" "bol_adm1.geojson"  -nln bol_map1
ogr2ogr -f "PostgreSQL" PG:"dbname=postgis user=postgres password=postgres" "bol_adm2.geojson"  -nln bol_map2
ogr2ogr -f "PostgreSQL" PG:"dbname=postgis user=postgres password=postgres" "bol_adm3.geojson"  -nln bol_map3