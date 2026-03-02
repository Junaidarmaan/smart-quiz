import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate, Navigate } from "react-router-dom";
import authApi from "../../api/authApi";
import { useAuth } from "../../context/AuthContext";
import { useTeddy } from "../../context/TeddyContext";
import {
  Box, Button, Card, CardContent, Container, Divider,
  IconButton, InputAdornment, Stack, TextField, Typography,
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import VisibilityOffOutlinedIcon from "@mui/icons-material/VisibilityOffOutlined";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";

export default function Login() {
  const { isAuthenticated, login, loading } = useAuth();
  const theme = useTheme();
  const navigate = useNavigate();
  const { showTeddy, hideTeddy } = useTeddy();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [submitAttempted, setSubmitAttempted] = useState(false);

  const isEmailValid = useMemo(() => {
    if (!email) return false;
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }, [email]);

  const isPasswordValid = useMemo(() => password.length >= 6, [password]);

  useEffect(() => {
    showTeddy({ message: "Ready to log you in!", emotion: "idle" });
    return () => hideTeddy();
  }, []);

  useEffect(() => {
    if (!email && !password && !submitAttempted) {
      showTeddy({ message: "Ready to log you in!", emotion: "idle" });
      return;
    }

    if (submitAttempted) {
      if (!email || !password) {
        showTeddy({ message: "Please fill all fields!", emotion: "angry" });
        return;
      }
      if (!isEmailValid || !isPasswordValid) {
        showTeddy({ message: "Please correct the errors above", emotion: "sad" });
        return;
      }
    }

    if (email.length > 0 && !isEmailValid) {
      showTeddy({ message: "That doesn't look like a valid email", emotion: "confused" });
    } else if (password.length > 0 && !isPasswordValid) {
      showTeddy({ message: "Password is a bit too short", emotion: "sad" });
    } else if (isEmailValid && isPasswordValid) {
      showTeddy({ message: "Looking good! Ready to submit?", emotion: "happy" });
    } else {
      showTeddy({ message: "Keep going...", emotion: "idle" });
    }
  }, [email, password, isEmailValid, isPasswordValid, submitAttempted]);

  const onSubmit = async (e) => {
    e.preventDefault();
    setSubmitAttempted(true);

    if (!email || !password || !isEmailValid || !isPasswordValid) return;

    try {
      const res = await authApi.login({ email, password });
      if (res.data.success) {
        showTeddy({ message: res.data.message || "Welcome back!", emotion: "happy" });
        login(res.data.token, res.data.username);
        setTimeout(() => {
          navigate("/dashboard");
          hideTeddy();
        }, 1500);
      } else {
        showTeddy({
          message: res.data.message || "Invalid credentials, please try again",
          emotion: "angry"
        });
      }
    } catch (error) {
      showTeddy({ message: "Something went wrong, please try again", emotion: "sad" });
    }
  };

  if (loading) return null;
  if (isAuthenticated) return <Navigate to="/dashboard" replace />;

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        py: { xs: 4, sm: 6 },
        bgcolor: "background.default",
      }}
    >
      <Container maxWidth="sm">
        <Card
          elevation={0}
          sx={{
            borderRadius: 4,
            border: `1px solid ${theme.palette.divider}`,
            bgcolor: "background.paper",
            boxShadow: "0 8px 30px rgba(0,0,0,0.35)",
          }}
        >
          <CardContent sx={{ p: { xs: 3, sm: 4 } }}>
            <Typography variant="h5" fontWeight={800} color="text.primary">
              Sign in
            </Typography>
            <Typography color="text.secondary" sx={{ mt: 0.5, mb: 3 }}>
              Enter your email and password to continue.
            </Typography>

            <Box component="form" onSubmit={onSubmit}>
              <Stack spacing={2.2}>
                <TextField
                  label="Email"
                  type="email"
                  fullWidth
                  value={email}
                  onChange={(e) => { setEmail(e.target.value); setSubmitAttempted(false); }}
                  autoComplete="email"
                  placeholder="you@example.com"
                  error={email.length > 0 && !isEmailValid}
                  helperText={email.length > 0 && !isEmailValid ? "Enter a valid email" : " "}
                />

                <TextField
                  label="Password"
                  type={showPassword ? "text" : "password"}
                  fullWidth
                  value={password}
                  onChange={(e) => { setPassword(e.target.value); setSubmitAttempted(false); }}
                  autoComplete="current-password"
                  placeholder="••••••••"
                  error={password.length > 0 && password.length < 6}
                  helperText={password.length > 0 && password.length < 6 ? "At least 6 characters" : " "}
                  InputProps={{
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton onClick={() => setShowPassword((s) => !s)} edge="end">
                          {showPassword ? <VisibilityOffOutlinedIcon /> : <VisibilityOutlinedIcon />}
                        </IconButton>
                      </InputAdornment>
                    ),
                  }}
                />

                <Button
                  type="submit"
                  variant="contained"
                  size="large"
                  sx={{ py: 1.25, fontWeight: 700, borderRadius: 3 }}
                >
                  Login
                </Button>

                <Divider />

                <Typography color="text.secondary" textAlign="center" sx={{ fontSize: "0.95rem" }}>
                  New here?{" "}
                  <Typography
                    component={Link}
                    to="/signup"
                    sx={{
                      color: "primary.main",
                      fontWeight: 700,
                      textDecoration: "none",
                      "&:hover": { textDecoration: "underline" },
                    }}
                  >
                    Create account
                  </Typography>
                </Typography>
              </Stack>
            </Box>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
}