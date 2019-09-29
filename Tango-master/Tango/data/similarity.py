import spacy
import json

npl = spacy.load('en_core_web_md')

genres = ("Rock & Roll/Rockabilly", "Tropical", "Classic Blues", "Bluegrass",
			"TV Soundtracks", "Baroque", "Urban Cowboy", "Indie Rock", "Romantic", "Country", "Nursery Rhymes",
			"Classical Period", "East Coast", "Electro", "Trance", "Delta Blues", "Old school soul", "Kids & Family",
			"Contemporary R&B", "Rock", "Dance", "Brazilian Music", "Comedy", "Pop", "Electro Pop/Electro Rock",
			"Grime", "Asian Music", "Modern", "Folk", "Bollywood", "Ranchera", "Contemporary Soul", "Game Scores",
			"Ska", "TV shows & movies", "Opera", "Rap/Hip Hop", "Jazz Hip Hop", "Instrumental jazz", "Soundtracks",
			"Sports", "Films/Games", "Metal", "Reggae", "Dubstep", "International Pop", "Alternative Country",
			"Electric Blues", "Dub", "Jazz", "Dancehall/Ragga", "Hard Rock", "Indian Music", "Classical", "Indie Pop",
			"Blues", "Soul & Funk", "Early Music", "West Coast", "Bolero", "Acoustic Blues", "Indie Rock/Rock pop",
			"Indie Pop/Folk", "Disco", "Spirituality & Religion", "R&B", "African Music", "Latin Music", "Kids",
			"Country Blues", "Electro Hip Hop", "Alternative", "Film Scores", "Vocal jazz", "Dirty South",
			"Traditional Country", "Old School", "Singer & Songwriter", "Dancefloor", "Chicago Blues", "Oldschool R&B",
			"Techno/House", "Chill Out/Trip-Hop/Lounge", "Musicals", "Norteño", "Corridos", "Stories", "Renaissance")


d = {}
for i in range(len(genres)):
    innerD = {}
    for j in range(len(genres)):
        x = npl(genres[i])
        y = npl(genres[j])
        innerD[genres[j]] = x.similarity(y)
    d[genres[i]] = innerD

    print(i)

with open("index.json", "w") as f:
    f.write(json.dumps(d))
