import React, { useMemo, useState } from 'react';
import { Check, Copy, Image as ImageIcon, Search, Share2, Star, Trash2 } from 'lucide-react';
import { SavedTextItem } from '../types/typography';

interface FavoritesViewProps {
  favorites: SavedTextItem[];
  copiedId: string | null;
  onCopy: (item: SavedTextItem) => void;
  onShare: (item: SavedTextItem) => void;
  onExportPng: (item: SavedTextItem) => void;
  onRemove: (id: string) => void;
  onClearAll: () => void;
  onGoToStudio: () => void;
}

export const FavoritesView: React.FC<FavoritesViewProps> = ({
  favorites,
  copiedId,
  onCopy,
  onShare,
  onExportPng,
  onRemove,
  onClearAll,
  onGoToStudio,
}) => {
  const [query, setQuery] = useState('');

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return favorites;
    return favorites.filter(
      (f) =>
        f.styleName.toLowerCase().includes(q) ||
        f.sourceText.toLowerCase().includes(q) ||
        f.styledText.toLowerCase().includes(q)
    );
  }, [favorites, query]);

  return (
    <section aria-label="Saved Favorites" className="space-y-5">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="font-syne text-2xl font-extrabold" style={{ color: 'var(--text-primary)' }}>
            Saved Favorites
          </h1>
          <p className="text-sm" style={{ color: 'var(--text-secondary)' }}>
            Stored locally on your device for instant offline access.
          </p>
        </div>

        {favorites.length > 0 && (
          <button
            type="button"
            onClick={onClearAll}
            className="inline-flex min-h-[40px] items-center gap-1.5 rounded-full border px-4 py-1.5 text-xs font-grotesk font-semibold transition-colors hover:border-red-400"
            style={{
              backgroundColor: 'var(--surface-card)',
              borderColor: 'var(--border-subtle)',
              color: 'var(--text-secondary)',
            }}
          >
            <Trash2 className="h-3.5 w-3.5" aria-hidden="true" />
            Clear All
          </button>
        )}
      </div>

      {favorites.length > 0 && (
        <div
          className="flex items-center rounded-2xl border px-3.5"
          style={{
            backgroundColor: 'var(--surface-card)',
            borderColor: 'var(--border-subtle)',
          }}
        >
          <Search className="h-4 w-4 shrink-0" style={{ color: 'var(--text-muted)' }} aria-hidden="true" />
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Filter saved favorites..."
            className="min-h-[44px] w-full bg-transparent px-3 text-sm outline-none"
            style={{ color: 'var(--text-primary)' }}
          />
        </div>
      )}

      {favorites.length === 0 ? (
        <div
          className="rounded-3xl border p-10 text-center"
          style={{
            backgroundColor: 'var(--surface-card)',
            borderColor: 'var(--border-subtle)',
          }}
        >
          <Star className="mx-auto h-10 w-10" style={{ color: 'var(--accent-gold)' }} aria-hidden="true" />
          <h2 className="mt-4 font-syne text-lg font-bold" style={{ color: 'var(--text-primary)' }}>
            No Favorites Saved Yet
          </h2>
          <p className="mt-1.5 text-sm max-w-md mx-auto" style={{ color: 'var(--text-secondary)' }}>
            Tap the star icon on any styled card in the Studio to pin your favorite English or Hindi typography here.
          </p>
          <button
            type="button"
            onClick={onGoToStudio}
            className="mt-5 inline-flex min-h-[44px] items-center justify-center rounded-full px-6 py-2 text-xs font-grotesk font-semibold text-white"
            style={{ background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)' }}
          >
            Open Typography Studio
          </button>
        </div>
      ) : filtered.length === 0 ? (
        <div
          className="rounded-2xl border p-8 text-center text-sm"
          style={{
            backgroundColor: 'var(--surface-card)',
            borderColor: 'var(--border-subtle)',
            color: 'var(--text-secondary)',
          }}
        >
          No favorites matched &ldquo;{query}&rdquo;.
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {filtered.map((item) => {
            const isCopied = copiedId === item.id;
            return (
              <article
                key={item.id}
                className="flex flex-col justify-between rounded-2xl border p-5"
                style={{
                  backgroundColor: 'var(--surface-card)',
                  borderColor: isCopied ? 'var(--accent-gold)' : 'var(--border-subtle)',
                }}
              >
                <div>
                  <div className="flex items-start justify-between gap-2">
                    <div>
                      <h3 className="font-syne text-base font-bold" style={{ color: 'var(--text-primary)' }}>
                        {item.styleName}
                      </h3>
                      <p className="text-xs mt-0.5" style={{ color: 'var(--text-secondary)' }}>
                        Saved {new Date(item.createdAt).toLocaleDateString()}
                      </p>
                    </div>
                    <button
                      type="button"
                      onClick={() => onRemove(item.id)}
                      aria-label={`Remove ${item.styleName} from favorites`}
                      className="inline-flex min-h-[38px] min-w-[38px] items-center justify-center rounded-full border hover:border-red-400"
                      style={{
                        backgroundColor: 'var(--surface-low)',
                        borderColor: 'var(--border-subtle)',
                        color: 'var(--text-secondary)',
                      }}
                    >
                      <Trash2 className="h-4 w-4" aria-hidden="true" />
                    </button>
                  </div>

                  <div
                    className="mt-3 rounded-xl border p-3.5"
                    style={{
                      backgroundColor: 'var(--surface-low)',
                      borderColor: 'var(--border-subtle)',
                    }}
                  >
                    <p
                      className={`text-lg whitespace-pre-wrap break-words devanagari-safe ${item.fontClass ?? ''}`}
                      style={{ color: 'var(--text-primary)' }}
                    >
                      {item.styledText}
                    </p>
                  </div>
                </div>

                <div className="mt-4 flex flex-wrap gap-2">
                  <button
                    type="button"
                    onClick={() => onCopy(item)}
                    className="flex-1 min-h-[40px] inline-flex items-center justify-center gap-1.5 rounded-full px-4 text-xs font-grotesk font-semibold"
                    style={
                      isCopied
                        ? { backgroundColor: 'var(--accent-gold)', color: '#1A1100' }
                        : {
                            background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)',
                            color: '#FFFFFF',
                          }
                    }
                  >
                    {isCopied ? <Check className="h-3.5 w-3.5" /> : <Copy className="h-3.5 w-3.5" />}
                    <span>{isCopied ? 'Copied' : 'Copy'}</span>
                  </button>
                  <button
                    type="button"
                    onClick={() => onShare(item)}
                    className="min-h-[40px] inline-flex items-center justify-center gap-1.5 rounded-full border px-3.5 text-xs font-grotesk font-semibold"
                    style={{
                      backgroundColor: 'var(--surface-elevated)',
                      borderColor: 'var(--border-subtle)',
                      color: 'var(--text-primary)',
                    }}
                  >
                    <Share2 className="h-3.5 w-3.5" />
                    <span>Share</span>
                  </button>
                  <button
                    type="button"
                    onClick={() => onExportPng(item)}
                    className="min-h-[40px] inline-flex items-center justify-center gap-1.5 rounded-full border px-3.5 text-xs font-grotesk font-semibold"
                    style={{
                      backgroundColor: 'var(--surface-elevated)',
                      borderColor: 'var(--border-subtle)',
                      color: 'var(--text-primary)',
                    }}
                  >
                    <ImageIcon className="h-3.5 w-3.5" style={{ color: 'var(--accent-gold)' }} />
                    <span>PNG</span>
                  </button>
                </div>
              </article>
            );
          })}
        </div>
      )}
    </section>
  );
};
