#!/bin/bash

# Obtener la carpeta del script
SCRIPT_DIR=$(dirname "$0")

# Escanear todos los archivos (no directorios) en la carpeta del script
for file in "$SCRIPT_DIR"/*; do
    # Verificar si es un archivo regular
    if [ -f "$file" ]; then
        # Obtener el nombre del archivo sin la ruta
        filename=$(basename "$file")

        # Convertir a minúsculas, reemplazar espacios y guiones por "_", y eliminar caracteres raros
        new_name=$(echo "$filename" | tr '[:upper:]' '[:lower:]' | tr ' -' '_' | sed 's/[^a-zA-Z0-9._]//g')

        # Verificar si el nombre ha cambiado
        if [ "$filename" != "$new_name" ]; then
            # Renombrar el archivo
            mv "$file" "$SCRIPT_DIR/$new_name"
            echo "Renombrado: $filename -> $new_name"
        fi
    fi
done

echo "Renombrado de archivos completado."

