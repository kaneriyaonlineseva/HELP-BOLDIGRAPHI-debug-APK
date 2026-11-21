import React from 'react';
import { Check, Copy, Image as ImageIcon, Share2, Star } from 'lucide-react';
import { StylePreset } from '../types/typography';

interface StyleCardProps {
  preset: StylePreset;
  renderedText: string;
  previewFontSize: number;
  isCopied: boolean;
  isFavorited: boolean;
  onCopy: () => void;
  onShare: () => void;
  onExportPng: () => void;
  onToggleFavorite: () => void;
}

export const StyleCard: React.FC<StyleCardProps> = ({
  preset,
  renderedText,
  previewFontSize,
  isCopied,
  isFavorited,
  onCopy,
  onShare,
  onExportPng,
  onToggleFavorite,
}) => {
  const scriptLabel =
    preset.script === 'hindi'
      ? 'हिन्दी Devanagari'
      : preset.script === 'both'
        ? 'English & हिन्दी'
        : 'English Unicode';

  const modeLabel =
    preset.renderMode === 'font'
      ? 'Font Render + PNG'
      : preset.renderMode === 'ornament'
        ? 'Ornament Frame'
        : 'Copy-Paste Unicode';

  return (
    <article
      className={`group flex flex-col justify-between rounded-2xl border p-5 transition-all duration-200 hover:border-violet-500/70 ${
        isCopied ? 'gold-halo' : ''
      }`}
      style={{
        backgroundColor: 'var(--surface-card)',
        borderColor: isCopied ? 'var(--accent-gold)' : 'var(--border-subtle)',
      }}
    >
      {/* Card Header */}
      <div>
        <div className="flex items-start justify-between gap-3">
          <div className="min-w-0">
            <div className="flex items-center gap-2">
              <h2
                className="font-syne text-base font-bold truncate"
                style={{ color: 'var(--text-primary)' }}
              >
                {preset.name}
              </h2>
              {preset.isVip && (
                <span
                  className="font-grotesk text-[11px] font-semibold tracking-wide shrink-0"
                  style={{ color: 'var(--accent-gold)' }}
                >
                  Featured
                </span>
              )}
            </div>
            <div
              className="mt-1 flex flex-wrap items-center gap-1.5 text-xs"
              style={{ color: 'var(--text-secondary)' }}
            >
              <span>{scriptLabel}</span>
              <span aria-hidden="true">·</span>
              <span>{modeLabel}</span>
            </div>
          </div>

          <button
            type="button"
            onClick={onToggleFavorite}
            aria-label={isFavorited ? `Remove ${preset.name} from favorites` : `Save ${preset.name} to favorites`}
            aria-pressed={isFavorited}
            className="inline-flex min-h-[40px] min-w-[40px] items-center justify-center rounded-full border transition-colors hover:border-amber-500/60 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-amber-500 shrink-0"
            style={{
              backgroundColor: 'var(--surface-low)',
              borderColor: isFavorited ? 'var(--accent-gold)' : 'var(--border-subtle)',
              color: isFavorited ? 'var(--accent-gold)' : 'var(--text-secondary)',
            }}
          >
            <Star
              className="h-4 w-4"
              fill={isFavorited ? 'currentColor' : 'none'}
              aria-hidden="true"
            />
          </button>
        </div>

        {/* Live Styled Typography Preview */}
        <div
          className="mt-4 min-h-[84px] rounded-xl border px-4 py-3.5 flex items-center"
          style={{
            backgroundColor: 'var(--surface-low)',
            borderColor: 'var(--border-subtle)',
          }}
        >
          <p
            className={`w-full whitespace-pre-wrap break-words devanagari-safe select-all ${preset.fontClass ?? ''}`}
            style={{
              color: 'var(--text-primary)',
              fontSize: `${previewFontSize}px`,
            }}
          >
            {renderedText}
          </p>
        </div>
      </div>

      {/* Footer Action Dock */}
      <div className="mt-4 flex flex-wrap items-center justify-between gap-2 pt-1">
        <button
          type="button"
          onClick={onCopy}
          className="inline-flex flex-1 min-h-[42px] items-center justify-center gap-2 rounded-full px-4 py-2 text-xs font-grotesk font-semibold whitespace-nowrap transition-transform active:scale-95 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-violet-500"
          style={
            isCopied
              ? {
                  backgroundColor: 'var(--accent-gold)',
                  color: '#1A1100',
                }
              : {
                  background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)',
                  color: '#FFFFFF',
                }
          }
        >
          {isCopied ? (
            <>
              <Check className="h-4 w-4" aria-hidden="true" />
              <span>Copied</span>
            </>
          ) : (
            <>
              <Copy className="h-4 w-4" aria-hidden="true" />
              <span>Copy Text</span>
            </>
          )}
        </button>

        <button
          type="button"
          onClick={onShare}
          aria-label={`Share ${preset.name}`}
          className="inline-flex min-h-[42px] items-center justify-center gap-1.5 rounded-full border px-3.5 py-2 text-xs font-grotesk font-semibold whitespace-nowrap transition-colors hover:border-violet-500 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-violet-500"
          style={{
            backgroundColor: 'var(--surface-elevated)',
            borderColor: 'var(--border-subtle)',
            color: 'var(--text-primary)',
          }}
        >
          <Share2 className="h-3.5 w-3.5" aria-hidden="true" />
          <span>Share</span>
        </button>

        <button
          type="button"
          onClick={onExportPng}
          aria-label={`Export ${preset.name} as PNG`}
          className="inline-flex min-h-[42px] items-center justify-center gap-1.5 rounded-full border px-3.5 py-2 text-xs font-grotesk font-semibold whitespace-nowrap transition-colors hover:border-amber-500 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-amber-500"
          style={{
            backgroundColor: 'var(--surface-elevated)',
            borderColor: 'var(--border-subtle)',
            color: 'var(--text-primary)',
          }}
        >
          <ImageIcon className="h-3.5 w-3.5" style={{ color: 'var(--accent-gold)' }} aria-hidden="true" />
          <span>PNG</span>
        </button>
      </div>
    </article>
  );
};
