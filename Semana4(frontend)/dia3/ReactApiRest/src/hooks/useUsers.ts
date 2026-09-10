import { useCallback, useEffect, useState } from "react";
import {
  createUser,
  deleteUser,
  getUsers,
  updateUser,
  type NewUser,
  type User,
} from "../services/userService";

export function useUsers() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [saving, setSaving] = useState(false);

  const reload = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setUsers(await getUsers());
    } catch (e) {
      setError(e instanceof Error ? e.message : "Error desconocido");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void reload();
  }, [reload]);

  async function addUser(body: NewUser) {
    setSaving(true);
    setError(null);
    try {
      const created = await createUser(body);
      setUsers((prev) => [...prev, { ...body, id: created.id }]);
      return true;
    } catch (e) {
      setError(e instanceof Error ? e.message : "No se pudo crear el usuario");
      return false;
    } finally {
      setSaving(false);
    }
  }

  async function editUser(id: number, body: NewUser) {
    setSaving(true);
    setError(null);
    try {
      await updateUser(id, body);
      setUsers((prev) =>
        prev.map((u) => (u.id === id ? { ...u, ...body, id } : u)),
      );
      return true;
    } catch (e) {
      setError(e instanceof Error ? e.message : "No se pudo actualizar");
      return false;
    } finally {
      setSaving(false);
    }
  }

  async function removeUser(id: number) {
    setSaving(true);
    setError(null);
    try {
      await deleteUser(id);
      setUsers((prev) => prev.filter((u) => u.id !== id));
      return true;
    } catch (e) {
      setError(e instanceof Error ? e.message : "No se pudo eliminar");
      return false;
    } finally {
      setSaving(false);
    }
  }

  return {
    users,
    loading,
    error,
    saving,
    reload,
    addUser,
    editUser,
    removeUser,
  };
}
