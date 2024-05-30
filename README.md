## API Key
Für eine funktionierende Wetter-Abfrage, muss ein persönlicher API-Key hinzugefügt werden.

Schritt 1: Erstelle einen API-Key auf der Website von [OpenWeather](https://home.openweathermap.org/api_keys).  

Schritt 2: Kopiere den Key und erstelle eine neue Datei keys.xml unter res/values. Fülle Sie nun so wie in der sample_keys.xml.
```bash
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="weather_api_key">hier kommt dein api key hin</string>
</resources>
```
Schritt 3: Lasse das Projekt einmal synchroniseren und baue es dann neu. Nun sollte deine eine BuildConfig.java aktualisiert worden sein und du kannst deinen API-Key dort finden.
