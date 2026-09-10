import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import Stack from "@mui/material/Stack";
import TextField from "@mui/material/TextField";
import { useEffect, useState } from "react";
import type { NewUser } from "../services/userService";
import "../assets/css/UserFormDialog.css";

type UserFormDialogProps = {
  open: boolean;
  title: string;
  initial?: NewUser;
  saving?: boolean;
  onClose: () => void;
  onSubmit: (data: NewUser) => void | Promise<void>;
};

const emptyForm: NewUser = { name: "", username: "", email: "" };

export function UserFormDialog({
  open,
  title,
  initial,
  saving = false,
  onClose,
  onSubmit,
}: UserFormDialogProps) {
  const [form, setForm] = useState<NewUser>(emptyForm);

  useEffect(() => {
    if (open) {
      setForm(initial ?? emptyForm);
    }
  }, [open, initial]);

  function handleChange(field: keyof NewUser) {
    return (event: React.ChangeEvent<HTMLInputElement>) => {
      setForm((prev) => ({ ...prev, [field]: event.target.value }));
    };
  }

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();
    await onSubmit({
      name: form.name.trim(),
      username: form.username.trim(),
      email: form.email.trim(),
    });
  }

  const valid =
    form.name.trim().length >= 2 &&
    form.username.trim().length >= 2 &&
    form.email.includes("@");

  return (
    <Dialog
      open={open}
      onClose={onClose}
      fullWidth
      maxWidth="sm"
      slotProps={{ paper: { className: "dialogPaper" } }}
    >
      <form onSubmit={handleSubmit}>
        <DialogTitle className="dialogTitle">
          <span className="titleText">{title}</span>
        </DialogTitle>
        <DialogContent className="dialogContent">
          <Stack spacing={2} sx={{ pt: 1 }}>
            <TextField
              label="Nombre"
              value={form.name}
              onChange={handleChange("name")}
              required
              fullWidth
              autoFocus
            />
            <TextField
              label="Usuario"
              value={form.username}
              onChange={handleChange("username")}
              required
              fullWidth
            />
            <TextField
              label="Email"
              type="email"
              value={form.email}
              onChange={handleChange("email")}
              required
              fullWidth
            />
          </Stack>
        </DialogContent>
        <DialogActions className="dialogActions">
          <Button className="cancelBtn" onClick={onClose} disabled={saving}>
            Cancelar
          </Button>
          <Button
            className="saveBtn"
            type="submit"
            variant="contained"
            disabled={!valid || saving}
          >
            {saving ? "Guardando…" : "Guardar"}
          </Button>
        </DialogActions>
      </form>
    </Dialog>
  );
}
