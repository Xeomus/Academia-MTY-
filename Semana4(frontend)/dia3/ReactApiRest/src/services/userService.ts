import { request } from "./api";

export type User = {
  id: number;
  name: string;
  username: string;
  email: string;
};

export function getUsers() {
  return request<User[]>("/users");
}

export type NewUser = Omit<User, "id">;

export function createUser(body: NewUser) {
  return request<User>("/users", {
    method: "POST",
    body: JSON.stringify(body),
  });
}

export function updateUser(id: number, body: NewUser) {
  return request<User>(`/users/${id}`, {
    method: "PUT",
    body: JSON.stringify({ ...body, id }),
  });
}

export function deleteUser(id: number) {
  return request<void>(`/users/${id}`, { method: "DELETE" });
}
