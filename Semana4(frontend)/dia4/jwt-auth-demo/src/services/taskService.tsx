import { httpClient } from "./httpClient";
import type { Task } from "../types";

export async function getTasksByProject(projectId: number): Promise<Task[]> {
  const { data } = await httpClient.get<Task[]>(`/projects/${projectId}/tasks`);

  return data;
}
