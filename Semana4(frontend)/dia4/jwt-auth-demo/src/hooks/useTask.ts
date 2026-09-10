import { useEffect, useState } from "react";
import { getTasksByProject } from "../services/taskService";
import { getApiErrorMessage } from "../services/httpClient";
import type { Task } from "../types";

interface UseTasksResult {
  tasks: Task[];
  loading: boolean;
  error: string | null;
}

export function useTasks(projectId: number): UseTasksResult {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    setLoading(true);
    setError(null);

    getTasksByProject(projectId)
      .then((data) => {
        if (!cancelled) {
          setTasks(data);
        }
      })
      .catch((err: unknown) => {
        if (!cancelled) {
          setError(getApiErrorMessage(err));
        }
      })
      .finally(() => {
        if (!cancelled) {
          setLoading(false);
        }
      });

    return () => {
      cancelled = true;
    };
  }, [projectId]);

  return {
    tasks,
    loading,
    error,
  };
}
