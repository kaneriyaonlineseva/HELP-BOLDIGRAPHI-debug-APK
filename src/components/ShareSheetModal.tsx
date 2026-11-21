import React from 'react';
import { Copy, ExternalLink, MessageCircle, Send, Share2, X } from 'lucide-react';
import {
  buildPlatformShareUrls,
  copyToClipboard,
  isWebShareSupported,
  nativeShareText,
} from '../services/shareService';

interface ShareSheetModalProps {
  isOpen: boolean;
  text: string;
  styleName: string;
  onClose: () => void;
  onNotify: (msg: string) => void;
  onRecordShare: () => void;
}

export const ShareSheetModal: React.FC<ShareSheetModalProps> = ({
  isOpen,
  text,
  styleName,
  onClose,
  onNotify,
  onRecordShare,
}) => {
  if (!isOpen) return null;

  const urls = buildPlatformShareUrls(text);

  const handleNativeShare = async () => {
    const res = await nativeShareText(`HELP BOLDIGRAPHI · ${styleName}`, text);
    if (res === 'shared') {
      onRecordShare();
      onNotify('Shared via system share sheet.');
      onClose();
    } else if (res === 'unsupported') {
      await copyToClipboard(text);
      onNotify('System share unavailable here — styled text copied to clipboard.');
    }
  };

  const handlePlatformClick = async (platform: string, copyFirst = false) => {
    if (copyFirst) {
      await copyToClipboard(text);
      onNotify(`Text copied! Paste directly inside ${platform}.`);
    } else {
      onNotify(`Opening ${platform} share...`);
    }
    onRecordShare();
  };

  return (
    <div
      className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/75 p-0 sm:p-4 backdrop-blur-sm"
      role="dialog"
      aria-modal="true"
      aria-labelledby="share-sheet-title"
    >
      <div
        className="w-full max-w-md rounded-t-3xl sm:rounded-3xl border p-5"
        style={{
          backgroundColor: 'var(--surface-elevated)',
          borderColor: 'var(--border-subtle)',
          boxShadow: '0 20px 45px -10px rgba(11, 11, 18, 0.95)',
        }}
      >
        <div className="flex items-center justify-between gap-3">
          <div>
            <h2
              id="share-sheet-title"
              className="font-syne text-lg font-bold"
              style={{ color: 'var(--text-primary)' }}
            >
              Share Styled Text
            </h2>
            <p className="text-xs" style={{ color: 'var(--text-secondary)' }}>
              {styleName}
            </p>
          </div>
          <button
            type="button"
            onClick={onClose}
            aria-label="Close share sheet"
            className="inline-flex min-h-[40px] min-w-[40px] items-center justify-center rounded-full border"
            style={{
              backgroundColor: 'var(--surface-card)',
              borderColor: 'var(--border-subtle)',
              color: 'var(--text-primary)',
            }}
          >
            <X className="h-4 w-4" aria-hidden="true" />
          </button>
        </div>

        {/* Preview Strip */}
        <div
          className="mt-4 max-h-28 overflow-y-auto rounded-2xl border p-3.5 text-sm devanagari-safe whitespace-pre-wrap break-words"
          style={{
            backgroundColor: 'var(--surface-low)',
            borderColor: 'var(--border-subtle)',
            color: 'var(--text-primary)',
          }}
        >
          {text}
        </div>

        {/* Share Targets */}
        <div className="mt-4 grid grid-cols-2 gap-2.5">
          <a
            href={urls.whatsapp}
            target="_blank"
            rel="noopener noreferrer"
            onClick={() => handlePlatformClick('WhatsApp')}
            className="flex min-h-[46px] items-center justify-center gap-2 rounded-2xl border px-4 py-2.5 text-xs font-grotesk font-semibold transition-colors hover:border-emerald-500"
            style={{
              backgroundColor: 'var(--surface-card)',
              borderColor: 'var(--border-subtle)',
              color: 'var(--text-primary)',
            }}
          >
            <MessageCircle className="h-4 w-4 text-emerald-500" aria-hidden="true" />
            <span>WhatsApp</span>
          </a>

          <a
            href={urls.telegram}
            target="_blank"
            rel="noopener noreferrer"
            onClick={() => handlePlatformClick('Telegram')}
            className="flex min-h-[46px] items-center justify-center gap-2 rounded-2xl border px-4 py-2.5 text-xs font-grotesk font-semibold transition-colors hover:border-sky-500"
            style={{
              backgroundColor: 'var(--surface-card)',
              borderColor: 'var(--border-subtle)',
              color: 'var(--text-primary)',
            }}
          >
            <Send className="h-4 w-4 text-sky-500" aria-hidden="true" />
            <span>Telegram</span>
          </a>

          <a
            href={urls.facebook}
            target="_blank"
            rel="noopener noreferrer"
            onClick={() => handlePlatformClick('Facebook', true)}
            className="flex min-h-[46px] items-center justify-center gap-2 rounded-2xl border px-4 py-2.5 text-xs font-grotesk font-semibold transition-colors hover:border-blue-500"
            style={{
              backgroundColor: 'var(--surface-card)',
              borderColor: 'var(--border-subtle)',
              color: 'var(--text-primary)',
            }}
          >
            <ExternalLink className="h-4 w-4 text-blue-500" aria-hidden="true" />
            <span>Facebook</span>
          </a>

          <a
            href={urls.instagram}
            target="_blank"
            rel="noopener noreferrer"
            onClick={() => handlePlatformClick('Instagram', true)}
            className="flex min-h-[46px] items-center justify-center gap-2 rounded-2xl border px-4 py-2.5 text-xs font-grotesk font-semibold transition-colors hover:border-pink-500"
            style={{
              backgroundColor: 'var(--surface-card)',
              borderColor: 'var(--border-subtle)',
              color: 'var(--text-primary)',
            }}
          >
            <ExternalLink className="h-4 w-4 text-pink-500" aria-hidden="true" />
            <span>Instagram</span>
          </a>
        </div>

        {/* Native System Share & Copy */}
        <div className="mt-3 flex flex-col sm:flex-row gap-2.5">
          <button
            type="button"
            onClick={async () => {
              const ok = await copyToClipboard(text);
              if (ok) {
                onRecordShare();
                onNotify('Copied styled text to clipboard.');
                onClose();
              }
            }}
            className="flex-1 min-h-[44px] inline-flex items-center justify-center gap-2 rounded-full border px-4 py-2 text-xs font-grotesk font-semibold transition-colors hover:border-amber-500"
            style={{
              backgroundColor: 'var(--surface-card)',
              borderColor: 'var(--border-subtle)',
              color: 'var(--text-primary)',
            }}
          >
            <Copy className="h-4 w-4" style={{ color: 'var(--accent-gold)' }} aria-hidden="true" />
            Copy Text
          </button>

          {isWebShareSupported() && (
            <button
              type="button"
              onClick={handleNativeShare}
              className="flex-1 min-h-[44px] inline-flex items-center justify-center gap-2 rounded-full px-4 py-2 text-xs font-grotesk font-semibold text-white"
              style={{ background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)' }}
            >
              <Share2 className="h-4 w-4" aria-hidden="true" />
              System Share
            </button>
          )}
        </div>
      </div>
    </div>
  );
};
