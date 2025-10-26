#!/usr/bin/env python3
import os
import urllib.request
from pathlib import Path

# Create directories
os.makedirs("uploads/logos", exist_ok=True)
os.makedirs("uploads/productos", exist_ok=True)

print("=" * 70)
print("DESCARGANDO IMÁGENES COHERENTES")
print("=" * 70)

# Placeholder image URLs (using placeholder.com as fallback)
# These are simple placeholder images that will work
images = {
    "logos": {
        "burger_palace.png": "https://via.placeholder.com/200x100/FF6B6B/FFFFFF?text=Burger+Palace",
        "pizzeria_don_pepe.png": "https://via.placeholder.com/200x100/FFA500/FFFFFF?text=Pizza+Don+Pepe",
        "sushi_roll.png": "https://via.placeholder.com/200x100/2E7D32/FFFFFF?text=Sushi+Roll",
    },
    "productos": {
        "hamburguesa_clasica.jpg": "https://via.placeholder.com/300x300/8B4513/FFFFFF?text=Hamburguesa",
        "hamburguesa_doble.jpg": "https://via.placeholder.com/300x300/654321/FFFFFF?text=Hamburguesa+Doble",
        "papas_francesas.jpg": "https://via.placeholder.com/300x300/FFB900/FFFFFF?text=Papas",
        "refresco.jpg": "https://via.placeholder.com/300x300/FF0000/FFFFFF?text=Refresco",
        "combo_burger.jpg": "https://via.placeholder.com/300x300/FF6347/FFFFFF?text=Combo",
        "pizza_margarita.jpg": "https://via.placeholder.com/300x300/DC143C/FFFFFF?text=Pizza+Margarita",
        "pizza_pepperoni.jpg": "https://via.placeholder.com/300x300/FF4500/FFFFFF?text=Pizza+Pepperoni",
        "pizza_vegetariana.jpg": "https://via.placeholder.com/300x300/228B22/FFFFFF?text=Pizza+Vegetariana",
        "empanada.jpg": "https://via.placeholder.com/300x300/A0522D/FFFFFF?text=Empanada",
        "garlic_bread.jpg": "https://via.placeholder.com/300x300/D4A574/FFFFFF?text=Garlic+Bread",
        "sushi_philadelphia.jpg": "https://via.placeholder.com/300x300/E8956B/FFFFFF?text=Sushi+Philadelphia",
        "sushi_california.jpg": "https://via.placeholder.com/300x300/F0A555/FFFFFF?text=Sushi+California",
        "sushi_picante.jpg": "https://via.placeholder.com/300x300/FF0000/FFFFFF?text=Sushi+Picante",
        "gyoza.jpg": "https://via.placeholder.com/300x300/DAA520/FFFFFF?text=Gyoza",
        "te_verde.jpg": "https://via.placeholder.com/300x300/228B22/FFFFFF?text=Te+Verde",
    }
}

# Create placeholder images locally (simpler approach)
print("\n📸 Creando imágenes locales...\n")

# Create simple image files using PIL if available, otherwise create placeholder HTML files
try:
    from PIL import Image, ImageDraw, ImageFont
    
    # Logos
    for filename, _ in images["logos"].items():
        filepath = f"uploads/logos/{filename}"
        if not os.path.exists(filepath):
            # Create a simple colored image
            img = Image.new('RGB', (200, 100), color=(255, 200, 0))
            draw = ImageDraw.Draw(img)
            text = filename.replace('_', ' ').replace('.png', '')
            draw.text((50, 40), text, fill=(0, 0, 0))
            img.save(filepath)
            print(f"  ✓ {filepath}")
    
    # Productos
    for filename, _ in images["productos"].items():
        filepath = f"uploads/productos/{filename}"
        if not os.path.exists(filepath):
            img = Image.new('RGB', (300, 300), color=(200, 150, 100))
            draw = ImageDraw.Draw(img)
            text = filename.replace('_', ' ').replace('.jpg', '')
            draw.text((80, 140), text, fill=(255, 255, 255))
            img.save(filepath)
            print(f"  ✓ {filepath}")

except ImportError:
    print("⚠️  PIL no instalado. Creando archivos de texto como placeholder...\n")
    
    for folder, files in images.items():
        for filename in files.keys():
            filepath = f"uploads/{folder}/{filename}"
            if not os.path.exists(filepath):
                # Create a simple text file as placeholder
                ext = filename.split('.')[-1]
                Path(filepath).touch()
                with open(filepath, 'w') as f:
                    f.write(f"Placeholder image: {filename}")
                print(f"  ✓ {filepath}")

print("\n" + "=" * 70)
print("✓ IMÁGENES CREADAS")
print("=" * 70)
