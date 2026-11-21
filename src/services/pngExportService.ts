import { PngAspectRatio, PngExportConfig, PngThemePresetId } from '../types/typography';

export interface PngThemePreset {
  id: PngThemePresetId;
  name: string;
  bgStart: string;
  bgEnd: string;
  borderColor: string;
  accentColor: string;
  textColor: string;
  mutedColor: string;
  glowColor: string;
}

export const PNG_THEME_PRESETS: PngThemePreset[] = [
  {
    id: 'obsidian-violet',
    name: 'Obsidian Violet',
    bgStart: '#141424',
    bgEnd: '#0B0B12',
    borderColor: '#3B2F63',
    accentColor: '#A078FF',
    textColor: '#F8FAFC',
    mutedColor: '#94A3B8',
    glowColor: 'rgba(139, 92, 246, 0.28)',
  },
  {
    id: 'royal-gold',
    name: 'Royal Gold Foil',
    bgStart: '#1B1610',
    bgEnd: '#0D0B08',
    borderColor: '#785113',
    accentColor: '#FBBF24',
    textColor: '#FFFBEB',
    mutedColor: '#D6B98C',
    glowColor: 'rgba(245, 158, 11, 0.25)',
  },
  {
    id: 'midnight-ink',
    name: 'Midnight Slate',
    bgStart: '#121826',
    bgEnd: '#080B12',
    borderColor: '#28354D',
    accentColor: '#7C3AED',
    textColor: '#F1F5F9',
    mutedColor: '#94A3B8',
    glowColor: 'rgba(124, 58, 237, 0.22)',
  },
  {
    id: 'crimson-velvet',
    name: 'Crimson Velvet',
    bgStart: '#231019',
    bgEnd: '#10070B',
    borderColor: '#5B2239',
    accentColor: '#F59E0B',
    textColor: '#FFF1F2',
    mutedColor: '#D6A6B7',
    glowColor: 'rgba(245, 158, 11, 0.2)',
  },
  {
    id: 'ivory-luxe',
    name: 'Ivory Editorial',
    bgStart: '#FFFFFF',
    bgEnd: '#EFEBF7',
    borderColor: '#D2C8E8',
    accentColor: '#6D28D9',
    textColor: '#13131A',
    mutedColor: '#5B5570',
    glowColor: 'rgba(109, 40, 217, 0.14)',
  },
];

export function getCanvasDimensions(aspectRatio: PngAspectRatio): { width: number; height: number } {
  switch (aspectRatio) {
    case '4:5':
      return { width: 1080, height: 1350 };
    case '16:9':
      return { width: 1280, height: 720 };
    case '1:1':
    default:
      return { width: 1080, height: 1080 };
  }
}

function wrapTextLines(
  ctx: CanvasRenderingContext2D,
  text: string,
  maxWidth: number
): string[] {
  const rawParagraphs = text.split('\n');
  const lines: string[] = [];

  for (const paragraph of rawParagraphs) {
    if (!paragraph.trim()) {
      lines.push('');
      continue;
    }
    const words = paragraph.split(/\s+/);
    let currentLine = '';

    for (const word of words) {
      const candidate = currentLine ? `${currentLine} ${word}` : word;
      if (ctx.measureText(candidate).width <= maxWidth) {
        currentLine = candidate;
      } else {
        if (currentLine) {
          lines.push(currentLine);
          currentLine = '';
        }
        // Handle single very long word by character chunks
        if (ctx.measureText(word).width > maxWidth) {
          let chunk = '';
          for (const ch of Array.from(word)) {
            if (ctx.measureText(chunk + ch).width > maxWidth && chunk) {
              lines.push(chunk);
              chunk = ch;
            } else {
              chunk += ch;
            }
          }
          currentLine = chunk;
        } else {
          currentLine = word;
        }
      }
    }
    if (currentLine) lines.push(currentLine);
  }
  return lines.length ? lines : [' '];
}

export async function renderTypographyPng(
  canvas: HTMLCanvasElement,
  config: PngExportConfig
): Promise<void> {
  if (typeof document !== 'undefined' && 'fonts' in document) {
    try {
      await document.fonts.ready;
    } catch {
      // Continue with available fonts
    }
  }

  const { width, height } = getCanvasDimensions(config.aspectRatio);
  canvas.width = width;
  canvas.height = height;

  const ctx = canvas.getContext('2d');
  if (!ctx) return;

  const theme =
    PNG_THEME_PRESETS.find((t) => t.id === config.themePreset) ?? PNG_THEME_PRESETS[0];

  // Background Gradient
  const bgGrad = ctx.createLinearGradient(0, 0, width, height);
  bgGrad.addColorStop(0, theme.bgStart);
  bgGrad.addColorStop(1, theme.bgEnd);
  ctx.fillStyle = bgGrad;
  ctx.fillRect(0, 0, width, height);

  // Subtle Radial Aura
  const aura = ctx.createRadialGradient(
    width * 0.5,
    height * 0.45,
    width * 0.05,
    width * 0.5,
    height * 0.45,
    width * 0.58
  );
  aura.addColorStop(0, theme.glowColor);
  aura.addColorStop(1, 'rgba(0,0,0,0)');
  ctx.fillStyle = aura;
  ctx.fillRect(0, 0, width, height);

  // Inner Luxury Frame
  const inset = 44;
  ctx.strokeStyle = theme.borderColor;
  ctx.lineWidth = 2.5;
  ctx.beginPath();
  ctx.roundRect(inset, inset, width - inset * 2, height - inset * 2, 32);
  ctx.stroke();

  // Corner Accent Marks
  const markLen = 28;
  ctx.strokeStyle = theme.accentColor;
  ctx.lineWidth = 3.5;
  const corners = [
    [inset + 24, inset + 24, 1, 1],
    [width - inset - 24, inset + 24, -1, 1],
    [inset + 24, height - inset - 24, 1, -1],
    [width - inset - 24, height - inset - 24, -1, -1],
  ];
  for (const [cx, cy, dx, dy] of corners) {
    ctx.beginPath();
    ctx.moveTo(cx, cy + dy * markLen);
    ctx.lineTo(cx, cy);
    ctx.lineTo(cx + dx * markLen, cy);
    ctx.stroke();
  }

  // Main Typography Setup
  const contentPadX = 110;
  const maxWidth = width - contentPadX * 2;
  const scaledFontPx = Math.round(config.fontSize * (width / 540));
  ctx.font = `600 ${scaledFontPx}px ${config.canvasFontFamily}`;
  ctx.fillStyle = theme.textColor;
  ctx.textBaseline = 'middle';

  const lines = wrapTextLines(ctx, config.text || 'HELP BOLDIGRAPHI', maxWidth);
  const lineHeight = Math.round(scaledFontPx * 1.6);
  const totalTextHeight = lines.length * lineHeight;

  const anchorX =
    config.alignment === 'left'
      ? contentPadX
      : config.alignment === 'right'
        ? width - contentPadX
        : width / 2;

  ctx.textAlign = config.alignment;

  const centerY = config.showWatermark ? height * 0.47 : height * 0.5;
  let startY = centerY - totalTextHeight / 2 + lineHeight / 2;

  // Clamp startY if text is very tall
  const minStartY = inset + 90;
  if (startY < minStartY) startY = minStartY;

  for (let i = 0; i < lines.length; i++) {
    const y = startY + i * lineHeight;
    if (y > height - inset - 110) break;
    ctx.fillText(lines[i], anchorX, y);
  }

  // Optional Footer Tagline Watermark
  if (config.showWatermark) {
    ctx.textAlign = 'center';
    ctx.fillStyle = theme.accentColor;
    ctx.font = '600 22px "Space Grotesk", "Inter", sans-serif';
    ctx.fillText('HELP BOLDIGRAPHI', width / 2, height - inset - 68);

    ctx.fillStyle = theme.mutedColor;
    ctx.font = '500 18px "Inter", sans-serif';
    ctx.fillText(`Your Text, Your Style!  ·  ${config.styleName}`, width / 2, height - inset - 36);
  }
}

export function canvasToPngBlob(canvas: HTMLCanvasElement): Promise<Blob | null> {
  return new Promise((resolve) => {
    canvas.toBlob((blob) => resolve(blob), 'image/png', 1.0);
  });
}

export function downloadPngBlob(blob: Blob, filename: string): void {
  const url = URL.createObjectURL(blob);
  const anchor = document.createElement('a');
  anchor.href = url;
  anchor.download = filename;
  document.body.appendChild(anchor);
  anchor.click();
  document.body.removeChild(anchor);
  setTimeout(() => URL.revokeObjectURL(url), 1500);
}
