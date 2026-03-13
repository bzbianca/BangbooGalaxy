from PIL import Image, ImageDraw
import numpy as np

def create_retro_main_menu():
    """Create retro main menu background with orange/black theme"""
    img = Image.new('RGB', (800, 600), color=(15, 15, 20))
    draw = ImageDraw.Draw(img)
    
    # Create retro grid pattern
    for x in range(0, 800, 20):
        draw.line([(x, 0), (x, 600)], fill=(255, 150, 0, 50), width=1)
    for y in range(0, 600, 20):
        draw.line([(0, y), (800, y)], fill=(255, 150, 0, 50), width=1)
    
    # Add retro scanlines
    for y in range(0, 600, 4):
        draw.line([(0, y), (800, y)], fill=(0, 0, 0, 100), width=1)
    
    # Add retro geometric shapes
    for i in range(5):
        x = np.random.randint(50, 750)
        y = np.random.randint(50, 550)
        size = np.random.randint(20, 60)
        draw.rectangle([x, y, x+size, y+size], outline=(255, 150, 0), width=2)
    
    # Add retro circles
    for i in range(8):
        x = np.random.randint(30, 770)
        y = np.random.randint(30, 570)
        radius = np.random.randint(10, 30)
        draw.ellipse([x-radius, y-radius, x+radius, y+radius], outline=(255, 200, 50), width=2)
    
    img.save('backgrounds/main_menu_bg.png')
    print("Created main_menu_bg.png")

def create_retro_game_bg():
    """Create retro game background with lane indicators"""
    img = Image.new('RGB', (800, 600), color=(10, 10, 15))
    draw = ImageDraw.Draw(img)
    
    # Create lane dividers
    lane_positions = [200, 300, 400, 500, 600]
    for x in lane_positions:
        draw.line([(x, 0), (x, 600)], fill=(255, 150, 0, 80), width=2)
        # Add glow effect
        for offset in [-2, 2]:
            draw.line([(x+offset, 0), (x+offset, 600)], fill=(255, 200, 50, 40), width=1)
    
    # Add retro grid pattern
    for x in range(0, 800, 40):
        draw.line([(x, 0), (x, 600)], fill=(255, 150, 0, 20), width=1)
    for y in range(0, 600, 40):
        draw.line([(0, y), (800, y)], fill=(255, 150, 0, 20), width=1)
    
    # Add scanlines
    for y in range(0, 600, 3):
        draw.line([(0, y), (800, y)], fill=(0, 0, 0, 80), width=1)
    
    # Add retro hexagon patterns
    for i in range(6):
        x = np.random.randint(100, 700)
        y = np.random.randint(100, 500)
        size = np.random.randint(15, 35)
        # Draw hexagon
        points = []
        for j in range(6):
            angle = j * 60
            px = x + size * np.cos(np.radians(angle))
            py = y + size * np.sin(np.radians(angle))
            points.append((px, py))
        draw.polygon(points, outline=(255, 100, 0), width=2)
    
    img.save('backgrounds/game_bg.png')
    print("Created game_bg.png")

def create_retro_song_selection():
    """Create retro song selection background"""
    img = Image.new('RGB', (800, 600), color=(20, 15, 25))
    draw = ImageDraw.Draw(img)
    
    # Create retro wave pattern
    for y in range(0, 600, 30):
        for x in range(0, 800, 20):
            intensity = int(128 + 127 * np.sin(x * 0.05) * np.cos(y * 0.05))
            color = (min(255, intensity), min(255, int(intensity * 0.6)), 0)
            draw.rectangle([x, y, x+18, y+28], fill=color, outline=(255, 150, 0))
    
    # Add scanlines
    for y in range(0, 600, 2):
        draw.line([(0, y), (800, y)], fill=(0, 0, 0, 120), width=1)
    
    # Add retro music notes symbols
    for i in range(12):
        x = np.random.randint(50, 750)
        y = np.random.randint(50, 550)
        # Draw simple music note shape
        draw.ellipse([x, y, x+15, y+10], fill=(255, 200, 50))
        draw.line([(x+12, y), (x+12, y-20)], fill=(255, 200, 50), width=2)
        draw.line([(x+12, y-20), (x+20, y-15)], fill=(255, 200, 50), width=2)
    
    img.save('backgrounds/song_selection_bg.png')
    print("Created song_selection_bg.png")

def create_retro_settings():
    """Create retro settings background"""
    img = Image.new('RGB', (800, 600), color=(25, 20, 30))
    draw = ImageDraw.Draw(img)
    
    # Create retro circuit pattern
    for x in range(0, 800, 50):
        for y in range(0, 600, 50):
            # Draw circuit node
            draw.ellipse([x-5, y-5, x+5, y+5], fill=(255, 150, 0))
            # Draw connections
            if x < 750:
                draw.line([(x+5, y), (x+45, y)], fill=(255, 150, 0, 100), width=2)
            if y < 550:
                draw.line([(x, y+5), (x, y+45)], fill=(255, 150, 0, 100), width=2)
    
    # Add scanlines
    for y in range(0, 600, 3):
        draw.line([(0, y), (800, y)], fill=(0, 0, 0, 90), width=1)
    
    # Add retro gear symbols
    for i in range(8):
        x = np.random.randint(100, 700)
        y = np.random.randint(100, 500)
        radius = np.random.randint(20, 40)
        # Draw gear
        draw.ellipse([x-radius, y-radius, x+radius, y+radius], outline=(255, 200, 50), width=3)
        # Draw gear teeth
        for angle in range(0, 360, 45):
            rad = np.radians(angle)
            x1 = x + (radius-5) * np.cos(rad)
            y1 = y + (radius-5) * np.sin(rad)
            x2 = x + (radius+5) * np.cos(rad)
            y2 = y + (radius+5) * np.sin(rad)
            draw.line([(x1, y1), (x2, y2)], fill=(255, 200, 50), width=2)
    
    img.save('backgrounds/settings_bg.png')
    print("Created settings_bg.png")

def create_retro_results():
    """Create retro results background"""
    img = Image.new('RGB', (800, 600), color=(15, 20, 25))
    draw = ImageDraw.Draw(img)
    
    # Create retro starfield
    for i in range(100):
        x = np.random.randint(0, 800)
        y = np.random.randint(0, 600)
        size = np.random.randint(1, 4)
        brightness = np.random.randint(100, 255)
        draw.ellipse([x, y, x+size, y+size], fill=(brightness, int(brightness*0.6), 0))
    
    # Add diagonal scanlines
    for i in range(-600, 800, 20):
        draw.line([(i, 0), (i+600, 600)], fill=(255, 150, 0, 30), width=1)
    
    # Add retro trophy symbols
    for i in range(6):
        x = np.random.randint(100, 700)
        y = np.random.randint(100, 500)
        # Draw trophy cup
        draw.rectangle([x-10, y-5, x+10, y+10], fill=(255, 200, 50))
        draw.rectangle([x-15, y+10, x+15, y+15], fill=(255, 200, 50))
        draw.rectangle([x-20, y+15, x+20, y+20], fill=(255, 150, 0))
    
    img.save('backgrounds/results_bg.png')
    print("Created results_bg.png")

if __name__ == "__main__":
    create_retro_main_menu()
    create_retro_game_bg()
    create_retro_song_selection()
    create_retro_settings()
    create_retro_results()
    print("All retro backgrounds created successfully!")
