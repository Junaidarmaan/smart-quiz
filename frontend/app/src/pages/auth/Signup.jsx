import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import authApi from "../../api/authApi";
import { profileStore } from "../../utils/storage";
import { useTeddy } from "../../context/TeddyContext";
import {
  Box, Button, Card, CardContent, Container, Divider,
  IconButton, InputAdornment, Stack, TextField, Typography,
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import VisibilityOffOutlinedIcon from "@mui/icons-material/VisibilityOffOutlined";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";

export default function Signup() {
  const theme = useTheme();
  const navigate = useNavigate();
  const { showTeddy, hideTeddy } = useTeddy();

  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [submitAttempted, setSubmitAttempted] = useState(false);

  const isEmailValid = useMemo(() => {
    if (!email) return false;
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }, [email]);

  const isPasswordValid = useMemo(() => password.length >= 6, [password]);

  const passwordMismatch = useMemo(() => {
    return confirmPassword.length > 0 && password !== confirmPassword;
  }, [password, confirmPassword]);

  useEffect(() => {
    showTeddy({ message: "Welcome! Let's create your account!", emotion: "idle" });
    return () => hideTeddy();
  }, []);

  useEffect(() => {
    if (!fullName && !email && !password && !confirmPassword && !submitAttempted) {
      showTeddy({ message: "Welcome! Let's create your account!", emotion: "idle" });
      return;
    }

    if (submitAttempted) {
      if (!fullName.trim()) {
        showTeddy({ message: "Tell me your name first!", emotion: "angry" });
        return;
      }
      if (!email.trim() || !password || !confirmPassword) {
        showTeddy({ message: "Fill all fields properly!", emotion: "angry" });
        return;
      }
      if (!isEmailValid) {
        showTeddy({ message: "That email is wrong, fix it!", emotion: "angry" });
        return;
      }
      if (!isPasswordValid) {
        showTeddy({ message: "Password too weak, make it stronger!", emotion: "angry" });
        return;
      }
      if (passwordMismatch) {
        showTeddy({ message: "Passwords don't match!", emotion: "angry" });
        return;
      }
    }

    if (email.length > 0 && !isEmailValid) {
      showTeddy({ message: "This email looks suspicious", emotion: "confused" });
    } else if (password.length > 0 && !isPasswordValid) {
      showTeddy({ message: "Too short, use at least 6 characters", emotion: "sad" });
    } else if (passwordMismatch) {
      showTeddy({ message: "Confirm password must match!", emotion: "angry" });
    } else if (fullName.trim().length >= 2 && isEmailValid && isPasswordValid && confirmPassword.length > 0 && !passwordMismatch) {
      showTeddy({ message: "Perfect! Your account is ready!", emotion: "happy" });
    } else {
      showTeddy({ message: "Keep going...", emotion: "idle" });
    }
  }, [fullName, email, password, confirmPassword, isEmailValid, isPasswordValid, passwordMismatch, submitAttempted]);

  const onSubmit = async (e) => {
    e.preventDefault();
    setSubmitAttempted(true);

    if (!fullName.trim() || !email.trim() || !password || !confirmPassword) return;
    if (!isEmailValid || !isPasswordValid || passwordMismatch) return;

    try {
      const res = await authApi.signup({ userName: fullName, email, password });
      if (res.data.success) {
        showTeddy({ message: "Account created! Redirecting...", emotion: "happy" });
        profileStore.set(res.data.userName);
        setTimeout(() => {
          navigate("/login");
          hideTeddy();
        }, 1500);
      } else {
        showTeddy({ message: res.data.message || "Signup failed, try again", emotion: "sad" });
      }
    } catch (error) {
      showTeddy({ message: "Something went wrong, please try again", emotion: "sad" });
    }
  };

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
      <Container maxWidth="md">
        <Stack direction={{ xs: "column", md: "row" }} spacing={{ xs: 3, md: 4 }} alignItems="stretch">

          {/* LEFT SIDE - Teddy */}
          <Box sx={{ flex: 1, display: "flex", alignItems: "center", justifyContent: "center" }}>
            <TeddyPanel theme={theme} />
          </Box>

          {/* RIGHT SIDE - Form */}
          <Box sx={{ flex: 1, display: "flex", justifyContent: "center" }}>
            <Card
              elevation={0}
              sx={{
                width: "100%",
                maxWidth: 460,
                borderRadius: 4,
                border: `1px solid ${theme.palette.divider}`,
                bgcolor: "background.paper",
                boxShadow: "0 8px 30px rgba(0,0,0,0.35)",
              }}
            >
              <CardContent sx={{ p: { xs: 3, sm: 4 } }}>
                <Typography variant="h5" fontWeight={800} color="text.primary">
                  Sign up
                </Typography>
                <Typography color="text.secondary" sx={{ mt: 0.5, mb: 3 }}>
                  Enter your details to create an account.
                </Typography>

                <Box component="form" onSubmit={onSubmit}>
                  <Stack spacing={2.2}>
                    <TextField
                      label="Full Name"
                      fullWidth
                      value={fullName}
                      onChange={(e) => { setFullName(e.target.value); setSubmitAttempted(false); }}
                      autoComplete="name"
                      placeholder="Your name"
                      error={submitAttempted && !fullName.trim()}
                      helperText={submitAttempted && !fullName.trim() ? "Name is required" : " "}
                    />

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
                      autoComplete="new-password"
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

                    <TextField
                      label="Confirm Password"
                      type={showConfirm ? "text" : "password"}
                      fullWidth
                      value={confirmPassword}
                      onChange={(e) => { setConfirmPassword(e.target.value); setSubmitAttempted(false); }}
                      autoComplete="new-password"
                      placeholder="••••••••"
                      error={passwordMismatch}
                      helperText={passwordMismatch ? "Passwords do not match" : " "}
                      InputProps={{
                        endAdornment: (
                          <InputAdornment position="end">
                            <IconButton onClick={() => setShowConfirm((s) => !s)} edge="end">
                              {showConfirm ? <VisibilityOffOutlinedIcon /> : <VisibilityOutlinedIcon />}
                            </IconButton>
                          </InputAdornment>
                        ),
                      }}
                    />

                    <Button
                      type="submit"
                      variant="contained"
                      size="large"
                      disabled={!fullName.trim() || !email.trim() || !password || !confirmPassword || !isEmailValid || !isPasswordValid || passwordMismatch}
                      sx={{ py: 1.25, fontWeight: 700, borderRadius: 3 }}
                    >
                      Create Account
                    </Button>

                    <Divider />

                    <Typography color="text.secondary" textAlign="center" sx={{ fontSize: "0.95rem" }}>
                      Already have an account?{" "}
                      <Typography
                        component={Link}
                        to="/login"
                        sx={{
                          color: "primary.main",
                          fontWeight: 700,
                          textDecoration: "none",
                          "&:hover": { textDecoration: "underline" },
                        }}
                      >
                        Login
                      </Typography>
                    </Typography>
                  </Stack>
                </Box>
              </CardContent>
            </Card>
          </Box>
        </Stack>
      </Container>
    </Box>
  );
}

// Extracted Teddy panel as separate component
function TeddyPanel({ theme }) {
  const { message, emotion } = useTeddy();

  const teddyMap = {
    idle: require("../../assets/teddy/idle.gif"),
    happy: require("../../assets/teddy/happy.gif"),
    sad: require("../../assets/teddy/sad.gif"),
    angry: require("../../assets/teddy/angry.gif"),
    confused: require("../../assets/teddy/confused.gif"),
  };

  return (
    <Box sx={{ width: "100%", maxWidth: 520, display: "flex", flexDirection: "column", alignItems: "center", gap: 2 }}>
      <Box
        sx={{
          width: { xs: 220, sm: 280, md: 320 },
          height: { xs: 220, sm: 280, md: 320 },
          borderRadius: 4,
          border: `1px solid ${theme.palette.divider}`,
          bgcolor: "background.paper",
          overflow: "hidden",
          boxShadow: "0 10px 30px rgba(0,0,0,0.35)",
        }}
      >
        <Box
          component="img"
          src={teddyMap[emotion] || teddyMap.idle}
          alt="Teddy"
          sx={{ width: "100%", height: "100%", objectFit: "cover" }}
        />
      </Box>

      {/* Speech bubble */}
      <Box
        sx={{
          width: "100%",
          borderRadius: 3,
          border: `1px solid ${theme.palette.divider}`,
          bgcolor: "background.paper",
          p: 2,
          position: "relative",
        }}
      >
        <Box
          sx={{
            position: "absolute",
            top: -8,
            left: 26,
            width: 16,
            height: 16,
            transform: "rotate(45deg)",
            bgcolor: "background.paper",
            borderLeft: `1px solid ${theme.palette.divider}`,
            borderTop: `1px solid ${theme.palette.divider}`,
          }}
        />
        <Typography fontWeight={800} sx={{ mb: 0.5 }} color="text.primary">
          Teddy Assistant 🧸
        </Typography>
        <Typography color="text.secondary">{message}</Typography>
      </Box>
    </Box>
  );
}