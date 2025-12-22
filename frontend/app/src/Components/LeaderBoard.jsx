import React from "react";
import {
  Box,
  Container,
  Typography,
  Paper
} from "@mui/material";
import { motion, AnimatePresence } from "framer-motion";
import { useEffect } from "react";

import quizBG from "./assets/quizBG.jpg"; // adjust path if needed


export default function LeaderBoard({ rankings, currentUserName }) {
  const topThree = rankings.slice(0, 3);
  const rest = rankings.slice(3);


  return (
    <Box
      minHeight="100vh"
      width="100%"
      sx={{
        backgroundImage: `url(${quizBG})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        position: "relative"
      }}
    >
      {/* Overlay */}
      <Box
        sx={{
          position: "absolute",
          inset: 0,
          backgroundColor: "rgba(0,0,0,0.45)"
        }}
      />

      <Container
        maxWidth="sm"
        sx={{
          position: "relative",
          zIndex: 1,
          paddingTop: 4,
          paddingBottom: 4
        }}
      >
        {/* Header */}
        <Typography
          variant="h5"
          textAlign="center"
          color="#fff"
          fontWeight={700}
          letterSpacing={1}
          mb={3}
        >
          LEADERBOARD
        </Typography>

        {/* Top 3 */}
        <Box
          display="flex"
          justifyContent="space-between"
          alignItems="flex-end"
          mb={4}
        >
          {topThree.map((user, index) => {
            const isFirst = index === 0;

            return (
              <Box
                key={user.userName}
                textAlign="center"
                sx={{
                  flex: 1,
                  transform: isFirst ? "scale(1.1)" : "scale(0.95)"
                }}
              >
                <Box
                  sx={{
                    width: isFirst ? 80 : 64,
                    height: isFirst ? 80 : 64,
                    borderRadius: "50%",
                    background:
                      "linear-gradient(135deg, #6a85f1, #8f9cff)",
                    margin: "0 auto",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    color: "#fff",
                    fontWeight: 700,
                    fontSize: "1.2rem",
                    boxShadow: "0 8px 20px rgba(0,0,0,0.4)"
                  }}
                >
                  {index + 1}
                </Box>

                <Typography
                  mt={1}
                  color="#fff"
                  fontWeight={600}
                  fontSize="0.95rem"
                >
                  {user.userName}
                </Typography>

                <Typography
                  color="#c7d2fe"
                  fontSize="0.85rem"
                >
                  {user.score} pts
                </Typography>
              </Box>
            );
          })}
        </Box>

        {/* Remaining rankings */}
        <Paper
          sx={{
            borderRadius: 4,
            padding: 2,
            maxHeight: "55vh",
            overflowY: "auto"
          }}
        >
          <AnimatePresence>
            {rest.map((user, index) => {
              const rank = index + 4;
              const isCurrentUser =
                user.userName === currentUserName;

              return (
                <motion.div
                  key={user.userName}
                  layout
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0 }}
                  transition={{ duration: 0.3 }}
                >
                  <Box
                    display="flex"
                    justifyContent="space-between"
                    alignItems="center"
                    paddingY={1.2}
                    paddingX={1}
                    mb={0.5}
                    sx={{
                      borderRadius: 2,
                      backgroundColor: isCurrentUser
                        ? "#eef2ff"
                        : "#ffffff"
                    }}
                  >
                    <Box display="flex" gap={1.5}>
                      <Typography
                        fontWeight={600}
                        color="#475569"
                      >
                        {rank}
                      </Typography>

                      <Typography
                        fontWeight={isCurrentUser ? 700 : 500}
                      >
                        {user.userName}
                      </Typography>
                    </Box>

                    <Typography
                      fontWeight={600}
                      color="#334155"
                    >
                      {user.score} pts
                    </Typography>
                  </Box>
                </motion.div>
              );
            })}
          </AnimatePresence>
        </Paper>
      </Container>
    </Box>
  );
}
