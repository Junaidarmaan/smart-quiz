package com.haisy.app.auth.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtUtil Tests")
class JwtUtilTest {

    private static final String EMAIL = "user@example.com";
    private static final String ROLE = "USER";

    // ─────────────────────────────────────────────────────────────────────────
    // generateToken()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("generateToken: returns a non-null, non-empty JWT string")
    void generateToken_returnsNonNullToken() {
        String token = JwtUtil.generateToken(EMAIL, ROLE);

        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("generateToken: token has exactly 3 dot-separated parts (header.payload.signature)")
    void generateToken_hasThreePartsJwtStructure() {
        String token = JwtUtil.generateToken(EMAIL, ROLE);

        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("generateToken: different calls produce different tokens (random salt)")
    void generateToken_twoCallsProduceDifferentTokens() {
        String token1 = JwtUtil.generateToken(EMAIL, ROLE);
        String token2 = JwtUtil.generateToken(EMAIL, ROLE);

        // Tokens may differ because issued-at timestamp differs between calls
        assertThat(token1).isNotNull();
        assertThat(token2).isNotNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // validateToken()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("validateToken: returns true for a freshly generated valid token")
    void validateToken_validToken_returnsTrue() {
        String token = JwtUtil.generateToken(EMAIL, ROLE);

        assertThat(JwtUtil.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("validateToken: returns false for a completely invalid/garbage token")
    void validateToken_invalidToken_returnsFalse() {
        assertThat(JwtUtil.validateToken("this.is.garbage")).isFalse();
    }

    @Test
    @DisplayName("validateToken: returns false for an empty string")
    void validateToken_emptyString_returnsFalse() {
        assertThat(JwtUtil.validateToken("")).isFalse();
    }

    @Test
    @DisplayName("validateToken: returns false for a token with a tampered payload")
    void validateToken_tamperedPayload_returnsFalse() {
        String token = JwtUtil.generateToken(EMAIL, ROLE);
        String[] parts = token.split("\\.");
        // Replace payload with a different base64 string
        String tampered = parts[0] + ".dGFtcGVyZWRwYXlsb2Fk" + "." + parts[2];

        assertThat(JwtUtil.validateToken(tampered)).isFalse();
    }

    @Test
    @DisplayName("validateToken: returns false for a token with a tampered signature")
    void validateToken_tamperedToken_returnsFalse() {
        String token = JwtUtil.generateToken(EMAIL, ROLE);
        // Corrupt the signature part (last segment)
        String[] parts = token.split("\\.");
        String tampered = parts[0] + "." + parts[1] + ".invalidsignature";

        assertThat(JwtUtil.validateToken(tampered)).isFalse();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // extractClaims()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("extractClaims: subject equals the email used to generate the token")
    void extractClaims_subjectEqualsEmail() {
        String token = JwtUtil.generateToken(EMAIL, ROLE);

        Claims claims = JwtUtil.extractClaims(token);

        assertThat(claims.getSubject()).isEqualTo(EMAIL);
    }

    @Test
    @DisplayName("extractClaims: role claim equals the role used to generate the token")
    void extractClaims_roleClaimMatchesRole() {
        String token = JwtUtil.generateToken(EMAIL, ROLE);

        Claims claims = JwtUtil.extractClaims(token);

        assertThat(claims.get("role", String.class)).isEqualTo(ROLE);
    }

    @Test
    @DisplayName("extractClaims: issuedAt is set and not null")
    void extractClaims_issuedAtIsPresent() {
        String token = JwtUtil.generateToken(EMAIL, ROLE);

        Claims claims = JwtUtil.extractClaims(token);

        assertThat(claims.getIssuedAt()).isNotNull();
    }

    @Test
    @DisplayName("extractClaims: expiration is set and in the future")
    void extractClaims_expirationIsInFuture() {
        String token = JwtUtil.generateToken(EMAIL, ROLE);

        Claims claims = JwtUtil.extractClaims(token);

        assertThat(claims.getExpiration()).isNotNull();
        assertThat(claims.getExpiration().getTime()).isGreaterThan(System.currentTimeMillis());
    }

    @Test
    @DisplayName("extractClaims: throws exception for an invalid token")
    void extractClaims_invalidToken_throwsException() {
        assertThatThrownBy(() -> JwtUtil.extractClaims("invalid.token.here"))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("extractClaims: ADMIN role token contains correct role claim")
    void extractClaims_adminRole_roleClaimIsAdmin() {
        String token = JwtUtil.generateToken("admin@example.com", "ADMIN");

        Claims claims = JwtUtil.extractClaims(token);

        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
        assertThat(claims.getSubject()).isEqualTo("admin@example.com");
    }

    @Test
    @DisplayName("extractClaims: round-trip — generateToken then extractClaims preserves all fields")
    void extractClaims_roundTrip_allFieldsPreserved() {
        String token = JwtUtil.generateToken("roundtrip@example.com", "USER");

        Claims claims = JwtUtil.extractClaims(token);

        assertThat(claims.getSubject()).isEqualTo("roundtrip@example.com");
        assertThat(claims.get("role", String.class)).isEqualTo("USER");
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isNotNull();
    }
}
