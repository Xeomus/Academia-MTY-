import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import Alert from "@mui/material/Alert";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import CircularProgress from "@mui/material/CircularProgress";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemText from "@mui/material/ListItemText";
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import { useNavigate, useParams } from "react-router-dom";
import { useTasks } from "../hooks/useTask";

export function ProjectTasksPage() {
  const navigate = useNavigate();
  const { projectId } = useParams();

  const numericProjectId = Number(projectId);
  const projectIdIsValid = Number.isInteger(numericProjectId);

  const { tasks, loading, error } = useTasks(numericProjectId);

  if (!projectIdIsValid) {
    return (
      <Box maxWidth={640} mx="auto" mt={6}>
        <Alert severity="error">
          El identificador del proyecto no es válido.
        </Alert>
      </Box>
    );
  }

  return (
    <Box maxWidth={640} mx="auto" mt={6}>
      <Stack direction="row" alignItems="center" spacing={2} mb={3}>
        <Button
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate("/dashboard")}
        >
          Volver
        </Button>

        <Typography variant="h4">Tareas del proyecto</Typography>
      </Stack>

      <Paper sx={{ p: 3 }}>
        <Typography variant="subtitle1" color="text.secondary" gutterBottom>
          Proyecto ID: {numericProjectId}
        </Typography>

        {loading && (
          <Stack alignItems="center" py={4}>
            <CircularProgress />
          </Stack>
        )}

        {!loading && error && <Alert severity="error">{error}</Alert>}

        {!loading && !error && tasks.length === 0 && (
          <Typography color="text.secondary">
            Este proyecto no tiene tareas.
          </Typography>
        )}

        {!loading && !error && tasks.length > 0 && (
          <List>
            {tasks.map((task) => (
              <ListItem key={task.id} divider>
                <ListItemText
                  primary={task.title}
                  secondary={task.description || `Estado: ${task.status}`}
                />
              </ListItem>
            ))}
          </List>
        )}
      </Paper>
    </Box>
  );
}
