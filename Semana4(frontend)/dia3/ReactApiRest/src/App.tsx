import Box from "@mui/material/Box";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
import { ThemeProvider } from "@mui/material/styles";
import { UsersPage } from "./pages/UsersPage";
import { theme } from "./theme";

export default function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ minHeight: "100vh", bgcolor: "background.default", py: 4 }}>
        <Container maxWidth="md">
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            Fase 3 — CRUD usuarios completo (GET · POST · PUT · DELETE)
          </Typography>
          <UsersPage />
        </Container>
      </Box>
    </ThemeProvider>
  );
}
