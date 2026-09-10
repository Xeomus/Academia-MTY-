import { useEffect, useState } from "react";
import {
  Avatar,
  Card,
  CardContent,
  CardHeader,
  Chip,
  CircularProgress,
  Typography,
} from "@mui/material";

import "./RickAndMortyService.css";

interface Character {
  id: number;
  name: string;
  image: string;
  species: string;
  status: string;
  gender: string;
}

function RickyAndMortyService() {
  const [characters, setCharacters] = useState<Character[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("https://rickandmortyapi.com/api/character")
      .then((response) => response.json())
      .then((data) => {
        setCharacters(data.results);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <div className="loading-container">
        <CircularProgress color="success" />
        <Typography>Cargando personajes...</Typography>
      </div>
    );
  }

  return (
    <main className="rick-page">
      <section className="characters-container">
        <div className="page-header">
          <Typography variant="h3" className="page-title">
            Rick and Morty
          </Typography>

          <Typography className="page-subtitle">
            Explora los personajes del multiverso
          </Typography>
        </div>

        <div className="character-grid">
          {characters.map((character) => (
            <Card className="character-card" key={character.id}>
              <div className="character-image-container">
                <Avatar
                  src={character.image}
                  alt={character.name}
                  className="character-avatar"
                />

                <Chip
                  label={character.status}
                  size="small"
                  className={`status-chip status-${character.status.toLowerCase()}`}
                />
              </div>

              <CardHeader
                className="character-header"
                title={character.name}
                subheader={character.species}
              />

              <CardContent className="character-content">
                <div className="character-info">
                  <span>Género</span>
                  <strong>{character.gender}</strong>
                </div>

                <div className="character-info">
                  <span>Especie</span>
                  <strong>{character.species}</strong>
                </div>

                <div className="character-info">
                  <span>ID</span>
                  <strong>#{character.id}</strong>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </section>
    </main>
  );
}

export default RickyAndMortyService;
