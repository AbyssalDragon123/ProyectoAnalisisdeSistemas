@echo off
echo Agregando archivos .gitkeep en carpetas vacías...

cd /d %~dp0

:: Crear .gitkeep en cada carpeta
type nul > backend\.gitkeep
type nul > frontend\.gitkeep
type nul > database\.gitkeep
type nul > documentacion\.gitkeep

echo Archivos .gitkeep creados.

:: Añadir a Git
git add .
git commit -m "Agregar .gitkeep para carpetas vacías"
git push

echo Proceso completado.
pause