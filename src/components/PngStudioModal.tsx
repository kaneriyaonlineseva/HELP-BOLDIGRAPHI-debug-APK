import React, { useEffect, useRef, useState } from 'react';
import {
  AlignCenter,
  AlignLeft,
  AlignRight,
  Download,
  Share2,
  X,
} from 'lucide-react';
import {
  PngAspectRatio,
  PngExportConfig,
  PngThemePresetId,
  TextAlignment,
} from '../types/typography';
import {
  PNG_THEME_PRESETS,
  canvasToPngBlob,
  downloadPngBlob,
  renderTypographyPng,
} from '../services/pngExportService';
import { isWebShareSupported, nativeSharePng } from '../services/shareService';

interface PngStudioModalProps {
  isOpen: boolean;
  initialText: string;
  styleName: string;
  canvasFontFamily: string;
  onClose: () => void;
  onNotify: (message: string) => void;
  onRecordExport: (text: string, styleName: string) => void;
}

export const PngStudioModal: React.FC<PngStudioModalProps> = ({
  isOpen,
  initialText,
  styleName,
  canvasFontFamily,
  onClose,
  onNotify,
  onRecordExport,
}) => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);
  const [config, setConfig] = useState<PngExportConfig>({
    text: initialText,
    styleName,
    canvasFontFamily,
    themePreset: 'obsidian-violet',
    aspectRatio: '1:1',
    fontSize: 34,
    alignment: 'center',
    showWatermark: true,
  });
  const [isExporting, setIsExporting] = useState(false);

  useEffect(() => {
    if (isOpen) {
      setConfig((prev) => ({
        ...prev,
        text: initialText,
        styleName,
        canvasFontFamily,
      }));
    }
  }, [isOpen, initialText, styleName, canvasFontFamily]);

  useEffect(() => {
    if (!isOpen || !canvasRef.current) return;
    let active = true;
    renderTypographyPng(canvasRef.current, config).then(() => {
      if (!active) return;
    });
    return () => {
      active = false;
    };
  }, [isOpen, config]);

  if (!isOpen) return null;

  const handleDownload = async () => {
    if (!canvasRef.current) return;
    setIsExporting(true);
    try {
      await renderTypographyPng(canvasRef.current, config);
      const blob = await canvasToPngBlob(canvasRef.current);
      if (!blob) {
        onNotify('Could not generate PNG image.');
        return;
      }
      const slug = styleName.toLowerCase().replace(/[^a-z0-9]+/g, '-');
      downloadPngBlob(blob, `help-boldigraphi-${slug}-${Date.now()}.png`);
      onRecordExport(config.text, styleName);
      onNotify('PNG image downloaded.');
    } finally {
      setIsExporting(false);
    }
  };

  const handleSharePng = async () => {
    if (!canvasRef.current) return;
    setIsExporting(true);
    try {
      await renderTypographyPng(canvasRef.current, config);
      const blob = await canvasToPngBlob(canvasRef.current);
      if (!blob) {
        onNotify('Could not generate PNG image.');
        return;
      }
      const result = await nativeSharePng(
        blob,
        `help-boldigraphi-${Date.now()}.png`,
        'HELP BOLDIGRAPHI',
        config.text
      );
      if (result === 'shared') {
        onRecordExport(config.text, styleName);
        onNotify('Shared PNG image.');
      } else if (result === 'unsupported') {
        downloadPngBlob(blob, `help-boldigraphi-${Date.now()}.png`);
        onRecordExport(config.text, styleName);
        onNotify('Direct file share unavailable here — PNG downloaded instead.');
      }
    } finally {
      setIsExporting(false);
    }
  };

  return (
    <div
      className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/75 p-0 sm:p-4 backdrop-blur-sm"
      role="dialog"
      aria-modal="true"
      aria-labelledby="png-studio-title"
    >
      <div
        className="flex max-h-[92vh] w-full max-w-4xl flex-col overflow-hidden rounded-t-3xl sm:rounded-3xl border"
        style={{
          backgroundColor: 'var(--surface-elevated)',
          borderColor: 'var(--border-subtle)',
          boxShadow: '0 20px 45px -10px rgba(11, 11, 18, 0.95)',
        }}
      >
        {/* Header */}
        <div
          className="flex items-center justify-between border-b px-5 py-4"
          style={{ borderColor: 'var(--border-subtle)' }}
        >
          <div>
            <h2
              id="png-studio-title"
              className="font-syne text-lg font-bold"
              style={{ color: 'var(--text-primary)' }}
            >
              Text-to-PNG Studio
            </h2>
            <p className="text-xs" style={{ color: 'var(--text-secondary)' }}>
              {styleName} · High-Resolution Export
            </p>
          </div>
          <button
            type="button"
            onClick={onClose}
            aria-label="Close PNG Studio"
            className="inline-flex min-h-[40px] min-w-[40px] items-center justify-center rounded-full border transition-colors hover:border-violet-500"
            style={{
              backgroundColor: 'var(--surface-card)',
              borderColor: 'var(--border-subtle)',
              color: 'var(--text-primary)',
            }}
          >
            <X className="h-4 w-4" aria-hidden="true" />
          </button>
        </div>

        {/* Body */}
        <div className="grid flex-1 grid-cols-1 lg:grid-cols-12 gap-5 overflow-y-auto p-5">
          {/* Canvas Preview */}
          <div className="lg:col-span-7 flex items-center justify-center rounded-2xl border p-4"
            style={{
              backgroundColor: 'var(--surface-low)',
              borderColor: 'var(--border-subtle)',
            }}
          >
            <canvas
              ref={canvasRef}
              className="max-h-[420px] w-auto max-w-full rounded-xl shadow-lg"
            />
          </div>

          {/* Studio Controls */}
          <div className="lg:col-span-5 space-y-4">
            {/* Editable Text */}
            <div>
              <label
                htmlFor="png-text-edit"
                className="block font-grotesk text-xs font-semibold mb-1.5"
                style={{ color: 'var(--text-secondary)' }}
              >
                Card Text
              </label>
              <textarea
                id="png-text-edit"
                rows={2}
                value={config.text}
                onChange={(e) => setConfig((c) => ({ ...c, text: e.target.value }))}
                className="w-full rounded-xl border px-3 py-2 text-sm outline-none devanagari-safe focus:border-violet-500"
                style={{
                  backgroundColor: 'var(--surface-card)',
                  borderColor: 'var(--border-subtle)',
                  color: 'var(--text-primary)',
                }}
              />
            </div>

            {/* Backdrop Theme */}
            <div>
              <div
                className="font-grotesk text-xs font-semibold mb-2"
                style={{ color: 'var(--text-secondary)' }}
              >
                Backdrop Theme
              </div>
              <div className="grid grid-cols-2 gap-2">
                {PNG_THEME_PRESETS.map((preset) => {
                  const selected = config.themePreset === preset.id;
                  return (
                    <button
                      key={preset.id}
                      type="button"
                      onClick={() =>
                        setConfig((c) => ({ ...c, themePreset: preset.id as PngThemePresetId }))
                      }
                      className="flex min-h-[40px] items-center gap-2 rounded-xl border px-3 py-2 text-left text-xs font-medium transition-all"
                      style={{
                        backgroundColor: 'var(--surface-card)',
                        borderColor: selected ? 'var(--accent-violet)' : 'var(--border-subtle)',
                        color: 'var(--text-primary)',
                      }}
                    >
                      <span
                        className="h-4 w-4 rounded-full shrink-0 border"
                        style={{
                          background: `linear-gradient(135deg, ${preset.bgStart}, ${preset.accentColor})`,
                          borderColor: preset.borderColor,
                        }}
                      />
                      <span className="truncate">{preset.name}</span>
                    </button>
                  );
                })}
              </div>
            </div>

            {/* Aspect Ratio & Alignment */}
            <div className="grid grid-cols-2 gap-3">
              <div>
                <div
                  className="font-grotesk text-xs font-semibold mb-1.5"
                  style={{ color: 'var(--text-secondary)' }}
                >
                  Aspect Ratio
                </div>
                <div className="flex gap-1.5">
                  {(['1:1', '4:5', '16:9'] as PngAspectRatio[]).map((ratio) => {
                    const active = config.aspectRatio === ratio;
                    return (
                      <button
                        key={ratio}
                        type="button"
                        onClick={() => setConfig((c) => ({ ...c, aspectRatio: ratio }))}
                        className="flex-1 min-h-[38px] rounded-lg border text-xs font-grotesk font-semibold transition-colors"
                        style={
                          active
                            ? {
                                backgroundColor: 'var(--accent-violet-bright)',
                                borderColor: 'var(--accent-violet)',
                                color: '#FFFFFF',
                              }
                            : {
                                backgroundColor: 'var(--surface-card)',
                                borderColor: 'var(--border-subtle)',
                                color: 'var(--text-secondary)',
                              }
                        }
                      >
                        {ratio}
                      </button>
                    );
                  })}
                </div>
              </div>

              <div>
                <div
                  className="font-grotesk text-xs font-semibold mb-1.5"
                  style={{ color: 'var(--text-secondary)' }}
                >
                  Alignment
                </div>
                <div className="flex gap-1.5">
                  {(
                    [
                      { id: 'left', icon: AlignLeft, label: 'Align left' },
                      { id: 'center', icon: AlignCenter, label: 'Align center' },
                      { id: 'right', icon: AlignRight, label: 'Align right' },
                    ] as const
                  ).map((item) => {
                    const Icon = item.icon;
                    const active = config.alignment === item.id;
                    return (
                      <button
                        key={item.id}
                        type="button"
                        aria-label={item.label}
                        onClick={() =>
                          setConfig((c) => ({ ...c, alignment: item.id as TextAlignment }))
                        }
                        className="flex-1 min-h-[38px] inline-flex items-center justify-center rounded-lg border transition-colors"
                        style={
                          active
                            ? {
                                backgroundColor: 'var(--accent-violet-bright)',
                                borderColor: 'var(--accent-violet)',
                                color: '#FFFFFF',
                              }
                            : {
                                backgroundColor: 'var(--surface-card)',
                                borderColor: 'var(--border-subtle)',
                                color: 'var(--text-secondary)',
                              }
                        }
                      >
                        <Icon className="h-4 w-4" aria-hidden="true" />
                      </button>
                    );
                  })}
                </div>
              </div>
            </div>

            {/* Font Size Slider */}
            <div>
              <div className="flex items-center justify-between mb-1.5">
                <label
                  htmlFor="png-font-size"
                  className="font-grotesk text-xs font-semibold"
                  style={{ color: 'var(--text-secondary)' }}
                >
                  Typography Scale
                </label>
                <span
                  className="font-grotesk text-xs font-semibold tabular-nums"
                  style={{ color: 'var(--text-primary)' }}
                >
                  {config.fontSize}pt
                </span>
              </div>
              <input
                id="png-font-size"
                type="range"
                min={20}
                max={64}
                step={2}
                value={config.fontSize}
                onChange={(e) => setConfig((c) => ({ ...c, fontSize: Number(e.target.value) }))}
                className="w-full accent-violet-500 cursor-pointer"
              />
            </div>

            {/* Brand Tagline Toggle */}
            <label
              className="flex min-h-[42px] cursor-pointer items-center justify-between rounded-xl border px-3.5 py-2"
              style={{
                backgroundColor: 'var(--surface-card)',
                borderColor: 'var(--border-subtle)',
              }}
            >
              <span className="text-xs font-medium" style={{ color: 'var(--text-primary)' }}>
                Include Brand Tagline Footer
              </span>
              <input
                type="checkbox"
                checked={config.showWatermark}
                onChange={(e) => setConfig((c) => ({ ...c, showWatermark: e.target.checked }))}
                className="h-4 w-4 accent-violet-500"
              />
            </label>
          </div>
        </div>

        {/* Footer Actions */}
        <div
          className="flex flex-wrap items-center justify-end gap-3 border-t px-5 py-4"
          style={{ borderColor: 'var(--border-subtle)' }}
        >
          {isWebShareSupported() && (
            <button
              type="button"
              disabled={isExporting}
              onClick={handleSharePng}
              className="inline-flex min-h-[44px] items-center justify-center gap-2 rounded-full border px-5 py-2 text-xs font-grotesk font-semibold transition-colors hover:border-violet-500 disabled:opacity-50"
              style={{
                backgroundColor: 'var(--surface-card)',
                borderColor: 'var(--border-subtle)',
                color: 'var(--text-primary)',
              }}
            >
              <Share2 className="h-4 w-4" aria-hidden="true" />
              Share PNG
            </button>
          )}
          <button
            type="button"
            disabled={isExporting}
            onClick={handleDownload}
            className="inline-flex min-h-[44px] items-center justify-center gap-2 rounded-full px-6 py-2 text-xs font-grotesk font-semibold text-white transition-transform active:scale-95 disabled:opacity-50"
            style={{ background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)' }}
          >
            <Download className="h-4 w-4" aria-hidden="true" />
            {isExporting ? 'Rendering...' : 'Download PNG'}
          </button>
        </div>
      </div>
    </div>
  );
};
