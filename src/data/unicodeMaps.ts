const UPPER = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
const LOWER = 'abcdefghijklmnopqrstuvwxyz';
const DIGITS = '0123456789';

function createOffsetMapper(
  upperStart: number,
  lowerStart: number,
  digitStart?: number,
  exceptions: Record<string, string> = {}
): (text: string) => string {
  return (text: string) =>
    Array.from(text)
      .map((ch) => {
        if (exceptions[ch]) return exceptions[ch];
        const uIdx = UPPER.indexOf(ch);
        if (uIdx !== -1) return String.fromCodePoint(upperStart + uIdx);
        const lIdx = LOWER.indexOf(ch);
        if (lIdx !== -1) return String.fromCodePoint(lowerStart + lIdx);
        if (digitStart !== undefined) {
          const dIdx = DIGITS.indexOf(ch);
          if (dIdx !== -1) return String.fromCodePoint(digitStart + dIdx);
        }
        return ch;
      })
      .join('');
}

function createMapMapper(map: Record<string, string>): (text: string) => string {
  return (text: string) =>
    Array.from(text)
      .map((ch) => map[ch] ?? ch)
      .join('');
}

function createCombiningMapper(combiningChar: string): (text: string) => string {
  return (text: string) =>
    Array.from(text)
      .map((ch) => (ch === ' ' || ch === '\n' ? ch : `${ch}${combiningChar}`))
      .join('');
}

// Mathematical Bold Serif
export const toBoldSerif = createOffsetMapper(0x1d400, 0x1d41a, 0x1d7ce);

// Mathematical Bold Sans
export const toBoldSans = createOffsetMapper(0x1d5d4, 0x1d5ee, 0x1d7ec);

// Mathematical Italic Serif
export const toItalicSerif = createOffsetMapper(0x1d434, 0x1d44e, undefined, {
  h: '\u210e',
});

// Mathematical Bold Italic Serif
export const toBoldItalicSerif = createOffsetMapper(0x1d468, 0x1d482);

// Mathematical Bold Italic Sans
export const toBoldItalicSans = createOffsetMapper(0x1d63c, 0x1d656);

// Mathematical Script (Calligraphy)
export const toScript = createOffsetMapper(0x1d49c, 0x1d4b6, undefined, {
  B: '\u212c',
  E: '\u2130',
  F: '\u2131',
  H: '\u210b',
  I: '\u2110',
  L: '\u2112',
  M: '\u2133',
  R: '\u211b',
  e: '\u212f',
  g: '\u210a',
  o: '\u2134',
});

// Mathematical Bold Script
export const toBoldScript = createOffsetMapper(0x1d4d0, 0x1d4ea);

// Mathematical Fraktur (Gothic)
export const toGothic = createOffsetMapper(0x1d504, 0x1d51e, undefined, {
  C: '\u212d',
  H: '\u210c',
  I: '\u2111',
  R: '\u211c',
  Z: '\u2128',
});

// Mathematical Bold Fraktur (Gothic Bold)
export const toGothicBold = createOffsetMapper(0x1d56c, 0x1d586);

// Mathematical Double-Struck
export const toDoubleStruck = createOffsetMapper(0x1d538, 0x1d552, 0x1d7d8, {
  C: '\u2102',
  H: '\u210d',
  N: '\u2115',
  P: '\u2119',
  Q: '\u211a',
  R: '\u211d',
  Z: '\u2124',
});

// Mathematical Monospace
export const toMonospace = createOffsetMapper(0x1d670, 0x1d68a, 0x1d7f6);

// Mathematical Sans-Serif
export const toSansClean = createOffsetMapper(0x1d5a0, 0x1d5ba, 0x1d7e2);

// Circled Letters
export const toCircled = createOffsetMapper(0x24b6, 0x24d0, undefined, {
  '0': '\u24ea',
  '1': '\u2460',
  '2': '\u2461',
  '3': '\u2462',
  '4': '\u2463',
  '5': '\u2464',
  '6': '\u2465',
  '7': '\u2466',
  '8': '\u2467',
  '9': '\u2468',
});

// Negative Circled (Dark Bubble)
const NEGATIVE_CIRCLED_MAP: Record<string, string> = {};
Array.from(UPPER).forEach((ch, idx) => {
  const glyph = String.fromCodePoint(0x1f150 + idx);
  NEGATIVE_CIRCLED_MAP[ch] = glyph;
  NEGATIVE_CIRCLED_MAP[ch.toLowerCase()] = glyph;
});
export const toDarkBubble = createMapMapper(NEGATIVE_CIRCLED_MAP);

// Squared Outline
const SQUARED_MAP: Record<string, string> = {};
Array.from(UPPER).forEach((ch, idx) => {
  const glyph = String.fromCodePoint(0x1f130 + idx);
  SQUARED_MAP[ch] = glyph;
  SQUARED_MAP[ch.toLowerCase()] = glyph;
});
export const toSquared = createMapMapper(SQUARED_MAP);

// Negative Squared (Black Boxed)
const NEGATIVE_SQUARED_MAP: Record<string, string> = {};
Array.from(UPPER).forEach((ch, idx) => {
  const glyph = String.fromCodePoint(0x1f170 + idx);
  NEGATIVE_SQUARED_MAP[ch] = glyph;
  NEGATIVE_SQUARED_MAP[ch.toLowerCase()] = glyph;
});
export const toBlackBoxed = createMapMapper(NEGATIVE_SQUARED_MAP);

// Small Caps
const SMALL_CAPS_MAP: Record<string, string> = {
  a: 'ᴀ', b: 'ʙ', c: 'ᴄ', d: 'ᴅ', e: 'ᴇ', f: 'ꜰ', g: 'ɢ', h: 'ʜ',
  i: 'ɪ', j: 'ᴊ', k: 'ᴋ', l: 'ʟ', m: 'ᴍ', n: 'ɴ', o: 'ᴏ', p: 'ᴘ',
  q: 'ǫ', r: 'ʀ', s: 's', t: 'ᴛ', u: 'ᴜ', v: 'ᴠ', w: 'ᴡ', x: 'x',
  y: 'ʏ', z: 'ᴢ',
};
export const toSmallCaps = createMapMapper(SMALL_CAPS_MAP);

// Fullwidth Aesthetic
export const toFullwidth = (text: string): string =>
  Array.from(text)
    .map((ch) => {
      if (ch === ' ') return '\u3000';
      const code = ch.charCodeAt(0);
      if (code >= 33 && code <= 126) {
        return String.fromCharCode(code + 0xfee0);
      }
      return ch;
    })
    .join('');

// Superscript Mini
const SUPERSCRIPT_MAP: Record<string, string> = {
  a: 'ᵃ', b: 'ᵇ', c: 'ᶜ', d: 'ᵈ', e: 'ᵉ', f: 'ᶠ', g: 'ᵍ', h: 'ʰ',
  i: 'ⁱ', j: 'ʲ', k: 'ᵏ', l: 'ˡ', m: 'ᵐ', n: 'ⁿ', o: 'ᵒ', p: 'ᵖ',
  q: 'ᵠ', r: 'ʳ', s: 'ˢ', t: 'ᵗ', u: 'ᵘ', v: 'ᵛ', w: 'ʷ', x: 'ˣ',
  y: 'ʸ', z: 'ᶻ',
  A: 'ᴬ', B: 'ᴮ', C: 'ᶜ', D: 'ᴰ', E: 'ᴱ', F: 'ᶠ', G: 'ᴳ', H: 'ᴴ',
  I: 'ᴵ', J: 'ᴶ', K: 'ᴷ', L: 'ᴸ', M: 'ᴹ', N: 'ᴺ', O: 'ᴼ', P: 'ᴾ',
  Q: 'Q', R: 'ᴿ', S: 'ˢ', T: 'ᵀ', U: 'ᵁ', V: 'ⱽ', W: 'ᵂ', X: 'ˣ',
  Y: 'ʸ', Z: 'ᶻ',
  '0': '⁰', '1': '¹', '2': '²', '3': '³', '4': '⁴',
  '5': '⁵', '6': '⁶', '7': '⁷', '8': '⁸', '9': '⁹',
};
export const toSuperscript = createMapMapper(SUPERSCRIPT_MAP);

// Upside Down Flipped
const FLIP_MAP: Record<string, string> = {
  a: 'ɐ', b: 'q', c: 'ɔ', d: 'p', e: 'ǝ', f: 'ɟ', g: 'ƃ', h: 'ɥ',
  i: 'ı', j: 'ɾ', k: 'ʞ', l: 'l', m: 'ɯ', n: 'u', o: 'o', p: 'd',
  q: 'b', r: 'ɹ', s: 's', t: 'ʇ', u: 'n', v: 'ʌ', w: 'ʍ', x: 'x',
  y: 'ʎ', z: 'z',
  A: '∀', B: '𐐒', C: 'Ɔ', D: 'ᗡ', E: 'Ǝ', F: 'Ⅎ', G: '⅁', H: 'H',
  I: 'I', J: 'ſ', K: '⋊', L: '˥', M: 'W', N: 'N', O: 'O', P: 'Ԁ',
  Q: 'Ό', R: 'ᴚ', S: 'S', T: '⊥', U: '∩', V: 'Λ', W: 'M', X: 'X',
  Y: '⅄', Z: 'Z',
  '1': 'Ɩ', '2': 'ᄅ', '3': 'Ɛ', '4': 'ㄣ', '5': 'ϛ', '6': '9',
  '7': 'ㄥ', '8': '8', '9': '6', '0': '0',
  '.': '˙', ',': '\'', '!': '¡', '?': '¿', '(': ')', ')': '(',
};
export const toUpsideDown = (text: string): string =>
  Array.from(text)
    .reverse()
    .map((ch) => FLIP_MAP[ch] ?? ch)
    .join('');

// Combining Effects
export const toStrikethrough = createCombiningMapper('\u0336');
export const toDoubleUnderline = createCombiningMapper('\u0333');
export const toSlashThrough = createCombiningMapper('\u0338');
export const toWaveAbove = createCombiningMapper('\u0303');

// Spaced Luxury Tracking
export const toSpacedLuxury = (text: string): string =>
  text
    .split('\n')
    .map((line) => Array.from(line.trim()).join(' '))
    .join('\n');

// Devanagari Numeral Converter (0-9 -> ०-९)
const DEVANAGARI_DIGITS: Record<string, string> = {
  '0': '०', '1': '१', '2': '२', '3': '३', '4': '४',
  '5': '५', '6': '६', '7': '७', '8': '८', '9': '९',
};
export const toDevanagariDigits = createMapMapper(DEVANAGARI_DIGITS);

export function containsDevanagari(text: string): boolean {
  return /[\u0900-\u097F]/.test(text);
}
