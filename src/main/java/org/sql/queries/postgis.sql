
-- ALL RECORDS FROM SAN CALIXTO
select count(*) from earthquakes_bolivia eb;
-- TOTAL ROWS: 3950


-- ALL RECORDS ONLY FOR BOLIVIA
select count(*) from bol_adm0 ba, earthquakes_bolivia eb
where ST_Contains(ba.wkb_geometry, eb.wkb_geometry);
-- TOTAL ROWS: 3856


-- CREATE A NEW TABLE ONLY FOR BOLIVIA
create table quakes_bol as
select eb.* from bol_adm0 ba, earthquakes_bolivia eb
where ST_Contains(ba.wkb_geometry, eb.wkb_geometry);

select count(*) from quakes_bol qb
-- TOTAL ROWS: 3865

-- DELETING SOME COLUMNS FOR quakes_bol TABLE
ALTER TABLE public.quakes_bol DROP COLUMN distance_text;
ALTER TABLE public.quakes_bol DROP COLUMN observations;
ALTER TABLE public.quakes_bol DROP COLUMN in_charge;
ALTER TABLE public.quakes_bol DROP COLUMN "references";


---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
----------------------------- ITERATE ALL FOR BOLIVIA ----------------------------------------------

create table eq_bol as
select qb.*, b.adm0_pcode, b.adm0_es from bol_adm0 b, quakes_bol qb
where ST_Intersects(b.wkb_geometry, qb.wkb_geometry)

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
----------------------------- ITERATE ALL DEPARTMENTS FOR BOLIVIA ----------------------------------------------

create table eq_bol_dep as
select qb.*, b.adm1_pcode, b.adm1_es from bol_adm1 b, eq_bol qb
where ST_Intersects(b.wkb_geometry, qb.wkb_geometry)


---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
----------------------------- ITERATE ALL PROVINCES FOR BOLIVIA ----------------------------------------------

create table eq_bol_prov as
select qb.*, b.adm2_pcode, b.adm2_es from bol_adm2 b, eq_bol_dep qb
where ST_Intersects(b.wkb_geometry, qb.wkb_geometry)


---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
----------------------------- ITERATE ALL MUNICIPALITIES FOR BOLIVIA ----------------------------------------------

create table eq_bol_mun as
select qb.*, b.adm3_pcode, b.adm3_es from bol_adm3 b, eq_bol_prov qb
where ST_Intersects(b.wkb_geometry, qb.wkb_geometry)


---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------

create table eq_bol as select * from eq_bol_mun

---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------


-- ALL RECORDS FOR BOLIVIA WITH MUNICIPALITIES

select count(*) from eq_bol eb;


-- CHECK THE MISSING RECORDS
--
select * from quakes_bol eb
where eb.id not in (select id from eq_bol eb2)

select * from eq_bol eb
where eb.id not in (select id from quakes_bol qb )

select * from earthquakes_bolivia eb
where eb.id not in (select id from quakes_bol eb2)


