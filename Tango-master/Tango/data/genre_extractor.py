import json

def extract(s):
    genreSet = set()
    
    with open(s, "r") as file:
        data = json.load(file)

        for key in data:
            genreSet = genreSet.union(set(data[key]))


    with open("genres.txt", "a") as genresFile:
        for genre in genreSet:
            genresFile.write(genre + "\n")
