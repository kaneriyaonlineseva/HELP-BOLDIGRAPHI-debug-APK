import React from 'react';
import { Check, Clock, Copy, Image as ImageIcon, Share2, Trash2 } from 'lucide-react';
import { SavedTextItem } from '../types/typography';

interface HistoryViewProps {
  history: SavedTextItem[];
  copiedId: string | null;
  onCopy: (item: SavedTextItem) => void;
  onShare: (item: SavedTextItem) => void;
  onExportPng: (item: SavedTextItem) => void;
  onRemove: (id: string) => void;
  onClearAll: () => void;
  onReuseSource: (text: string) => void;
  onGoToStudio: () => void;
}

export const HistoryView: React.FC<HistoryViewProps> = ({
  history,
  copiedId,
  onCopy,
  onShare,
  onExportPng,
  onRemove,
  onClearAll,
  onReuseSource,
  onGoToStudio,
}) => {
  return (
    <section aria-label="Recent History" className="space-y-5">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="font-syne text-2xl font-extrabold" style={{ color: 'var(--text-primary)' }}>
            Recent History
          </h1>
          <p className="text-sm" style={{ color: 'var(--text-secondary)' }}>
            Every copied, shared, or exported creation is saved automatically offline.
          </p>
        </div>

        {history.length > 0 && (
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
            Clear History
          </button>
        )}
      </div>

      {history.length === 0 ? (
        <div
          className="rounded-3xl border p-10 text-center"
          style={{
            backgroundColor: 'var(--surface-card)',
            borderColor: 'var(--border-subtle)',
          }}
        >
          <Clock className="mx-auto h-10 w-10" style={{ color: 'var(--accent-violet)' }} aria-hidden="true" />
          <h2 className="mt-4 font-syne text-lg font-bold" style={{ color: 'var(--text-primary)' }}>
            History Is Empty
          </h2>
          <p className="mt-1.5 text-sm max-w-md mx-auto" style={{ color: 'var(--text-secondary)' }}>
            Copy, share, or export any styled text in the Studio and it will appear here for quick reuse.
          </p>
          <button
            type="button"
            onClick={onGoToStudio}
            className="mt-5 inline-flex min-h-[44px] items-center justify-center rounded-full px-6 py-2 text-xs font-grotesk font-semibold text-white"
            style={{ background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)' }}
          >
            Start Creating
          </button>
        </div>
      ) : (
        <div className="space-y-3">
          {history.map((item) => {
            const isCopied = copiedId === item.id;
            return (
              <article
                key={item.id}
                className="rounded-2xl border p-4 sm:p-5"
                style={{
                  backgroundColor: 'var(--surface-card)',
                  borderColor: isCopied ? 'var(--accent-gold)' : 'var(--border-subtle)',
                }}
              >
                <div className="flex flex-wrap items-center justify-between gap-2">
                  <div className="flex flex-wrap items-center gap-2 text-xs">
                    <span className="font-syne text-sm font-bold" style={{ color: 'var(--text-primary)' }}>
                      {item.styleName}
                    </span>
                    <span aria-hidden="true" style={{ color: 'var(--text-muted)' }}>·</span>
                    <span className="font-grotesk capitalize" style={{ color: 'var(--accent-gold)' }}>
                      {item.actionType ?? 'copied'}
                    </span>
                    <span aria-hidden="true" style={{ color: 'var(--text-muted)' }}>·</span>
                    <span className="font-grotesk tabular-nums" style={{ color: 'var(--text-secondary)' }}>
                      {new Date(item.createdAt).toLocaleString()}
                    </span>
                  </div>

                  <button
                    type="button"
                    onClick={() => onRemove(item.id)}
                    aria-label="Delete history item"
                    className="inline-flex min-h-[36px] min-w-[36px] items-center justify-center rounded-full border hover:border-red-400"
                    style={{
                      backgroundColor: 'var(--surface-low)',
                      borderColor: 'var(--border-subtle)',
                      color: 'var(--text-secondary)',
                    }}
                  >
                    <Trash2 className="h-3.5 w-3.5" aria-hidden="true" />
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
                    className={`text-base sm:text-lg whitespace-pre-wrap break-words devanagari-safe ${item.fontClass ?? ''}`}
                    style={{ color: 'var(--text-primary)' }}
                  >
                    {item.styledText}
                  </p>
                </div>

                <div className="mt-3 flex flex-wrap items-center gap-2">
                  <button
                    type="button"
                    onClick={() => onCopy(item)}
                    className="min-h-[38px] inline-flex items-center gap-1.5 rounded-full px-4 text-xs font-grotesk font-semibold"
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
                    className="min-h-[38px] inline-flex items-center gap-1.5 rounded-full border px-3.5 text-xs font-grotesk font-semibold"
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
                    className="min-h-[38px] inline-flex items-center gap-1.5 rounded-full border px-3.5 text-xs font-grotesk font-semibold"
                    style={{
                      backgroundColor: 'var(--surface-elevated)',
                      borderColor: 'var(--border-subtle)',
                      color: 'var(--text-primary)',
                    }}
                  >
                    <ImageIcon className="h-3.5 w-3.5" style={{ color: 'var(--accent-gold)' }} />
                    <span>PNG</span>
                  </button>
                  <button
                    type="button"
                    onClick={() => onReuseSource(item.sourceText)}
                    className="min-h-[38px] inline-flex items-center gap-1.5 rounded-full border px-3.5 text-xs font-grotesk font-semibold ml-auto"
                    style={{
                      backgroundColor: 'var(--surface-low)',
                      borderColor: 'var(--border-subtle)',
                      color: 'var(--text-secondary)',
                    }}
                  >
                    Edit in Studio
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
