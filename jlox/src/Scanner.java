package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scanner
 */
public class Scanner {

	private final String source;
	private final List<Token> tokens = new ArrayList<>(); // stores the list of tokens for later use.
	private int start = 0; // first charactar in the lexeme
	private int current = 0; // current charactar being considered
	private int line = 1; // what line in the source the charactar is on

	private static final Map<String, TokenType> keywords;

	static {
		keywords = new HashMap<>();
		keywords.put("and", TokenType.AND);
		keywords.put("class", TokenType.CLASS);
		keywords.put("else", TokenType.ELSE);
		keywords.put("false", TokenType.FALSE);
		keywords.put("for", TokenType.FOR);
		keywords.put("fun", TokenType.FUN);
		keywords.put("if", TokenType.IF);
		keywords.put("nil", TokenType.NIL);
		keywords.put("or", TokenType.OR);
		keywords.put("print", TokenType.PRINT);
		keywords.put("return", TokenType.RETURN);
		keywords.put("super", TokenType.SUPER);
		keywords.put("this", TokenType.THIS);
		keywords.put("true", TokenType.TRUE);
		keywords.put("var", TokenType.VAR);
		keywords.put("while", TokenType.WHILE);
	}

	Scanner(String source) {
		this.source = source;
	}

	List<Token> scanTokens() {

		while (!isAtEnd()) {

			// We start at the beginning of the next lexeme.
			start = current;
			scanToken();
		}

		tokens.add(new Token(TokenType.EOF, "", null, line));
		return tokens;
	}

	private void scanToken() {
		char c = advance(); // gets the next char

		switch (c) {
			case '(':
				addToken(TokenType.LEFT_PAREN);
				break;
			case ')':
				addToken(TokenType.RIGHT_PAREN);
				break;
			case '{':
				addToken(TokenType.LEFT_BRACE);
				break;
			case '}':
				addToken(TokenType.RIGHT_BRACE);
				break;
			case ',':
				addToken(TokenType.COMMA);
				break;
			case '.':
				addToken(TokenType.DOT);
				break;
			case '-':
				addToken(TokenType.MINUS);
				break;
			case '+':
				addToken(TokenType.PLUS);
				break;
			case ';':
				addToken(TokenType.SEMICOLON);
				break;
			case '*':
				addToken(TokenType.STAR);
				break;
			case '!':
				addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
				break;
			case '=':
				addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
				break;
			case '<':
				addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
				break;
			case '>':
				addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
				break;
			case '/':
				if (match('/')) {
					// Comment goes to the end of the line
					while (peek() != '\n' && !isAtEnd()) {
						advance();
					}
				} else {
					addToken(TokenType.SLASH);
				}
				break;
			case ' ':
			case '\r':
			case '\t':

				// Ignore whitespace
				break;

			case '\n':
				line++;
				break;
			case '"':
				string();
				break;

			default:
				if (isDigit(c)) {

					number();

				} else if (isAlpha(c)) {

					identifier();

				} else {

					lox.error(line, "Unexpected character"); // produces an error
				}

				break;
		}
	}

	// handles identifiers in our langue.
	private void identifier() {
		while (isAlphaNumeric(peek())) {
			advance();
		}

		String text = source.substring(start, current);
		TokenType type = keywords.get(text);

		if (type == null) {
			type = TokenType.IDENTIFIER;
		}

		addToken(type);
	}

	// Handles numbers in our langue.
	private void number() {
		while (isDigit(peek())) {
			advance();
		}

		// looks for a fractional part.
		if (peek() == '.' && isDigit(peekNext())) {

			// consume the "."
			advance();

			while (isDigit(peek())) {
				advance();
			}
		}

		addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));

	}

	// processes the string literal.
	private void string() {
		while (peek() != '"' && !isAtEnd()) {

			if (peek() == '\n') {

				line++;

			}
			advance();
		}

		if (isAtEnd()) {
			lox.error(line, "Unterminated string.");
			return;
		}

		// Gets the closing '"'.
		advance();

		String value = source.substring(start + 1, current - 1);
		addToken(TokenType.STRING, value);
	}

	// Checks if the next charactar is what is expected.
	private boolean match(char expected) {
		if (isAtEnd()) {
			return false;
		}

		if (source.charAt(current) != expected) {
			return false;
		}

		current++;
		return true;
	}

	// Looks ahead by on char.
	private char peek() {
		if (isAtEnd())
			return '\0';
		return source.charAt(current);
	}

	// looks ahead by two chars.
	private char peekNext() {
		if (current + 1 >= source.length()) {

			return '\0';
		}

		return source.charAt(current + 1);
	}

	// checks if a char is a alphabatic char.
	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') ||
				(c >= 'A' && c <= 'Z') ||
				c == '_';
	}

	private boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}

	// Checks if a number is a digit.
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	// tells us if we consumed all charactars.
	private boolean isAtEnd() {
		return current >= source.length();
	}

	// This function gets the next char of the source file.
	private char advance() {
		return source.charAt(current++); // post inc, gets the char then inc by one.
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}

	// Creates a new token for the current lexeme
	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}
}
