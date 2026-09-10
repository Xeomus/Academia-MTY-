import Alert from '@mui/material/Alert'
import Box from '@mui/material/Box'
import CircularProgress from '@mui/material/CircularProgress'
import type { ReactNode } from 'react'

export function AsyncState({
  loading, error, empty, emptyMessage = 'Sin resultados.', children,
}: {
  loading: boolean; error: string | null; empty: boolean
  emptyMessage?: string; children: ReactNode
}) {
  if (loading) return <Box py={6} textAlign="center"><CircularProgress /></Box>
  if (error) return <Alert severity="error">{error}</Alert>
  if (empty) return <Alert severity="info">{emptyMessage}</Alert>
  return <>{children}</>
}