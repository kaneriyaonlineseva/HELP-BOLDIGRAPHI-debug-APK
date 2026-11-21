import React from 'react';
import { Eraser, Languages, Type } from 'lucide-react';
import { ScriptFilter } from '../types/typography';
import { containsDevanagari } from '../data/unicodeMaps';

interface MasterEditorProps {
  inputText: string;
  onInputChange: (text: string) => void;
  scriptFilter: ScriptFilter;
  onScriptFilterChange: (script: ScriptFilter) => void;
  previewFontSize: number;
  onPreviewFontSizeChange: (size: number) => void;
}

const SAMPLE_PROMPTS: { label: string; text: string; script: ScriptFilter }[] = [
  { label: 'Bilingual Brand', text: 'HELP BOLDIGRAPHI\nआपका स्वागत है', script: 'all' },
  { label: 'English Quote', text: 'Your Text, Your Style!', script: 'english' },
  { label: 'हिन्दी शायरी', text: 'शब्दों की अपनी कलाकारी है,\nहर अक्षर एक पहचान है।', script: 'hindi' },
  { label: 'शुभकामना', text: 'शुभ प्रभात · सुप्रभात २०२६', script: 'hindi' },
];

export const MasterEditor: React.FC<MasterEditorProps> = ({
  inputText,
  onInputChange,
  scriptFilter,
  onScriptFilterChange,
  previewFontSize,
  onPreviewFontSizeChange,
}) => {
  const charCount = Array.from(inputText).length;
  const wordCount = inputText.trim() ? inputText.trim().split(/\s+/).length : 0;
  const hasHindi = containsDevanagari(inputText);

  return (
    <section
      aria-label="Master Text Editor"
      className="obsidian-halo rounded-3xl border p-5 transition-all"
      style={{
        backgroundColor: 'var(--surface-card)',
        borderColor: 'var(--border-subtle)',
      }}
    >
      {/* Studio Tagline & Script Switcher */}
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1
            className="font-syne text-xl sm:text-2xl font-extrabold tracking-tight"
            style={{ color: 'var(--text-primary)' }}
          >
            Your Text, Your Style!
          </h1>
          <p className="mt-0.5 text-xs sm:text-sm" style={{ color: 'var(--text-secondary)' }}>
            English Unicode & हिन्दी देवनागरी Studio
          </p>
        </div>

        {/* Script Capsule Tabs */}
        <div
          className="inline-flex items-center rounded-full border p-1"
          style={{
            backgroundColor: 'var(--surface-low)',
            borderColor: 'var(--border-subtle)',
          }}
          role="tablist"
          aria-label="Script filter"
        >
          {(
            [
              { id: 'all', label: 'All' },
              { id: 'english', label: 'English' },
              { id: 'hindi', label: 'हिन्दी' },
            ] as const
          ).map((tab) => {
            const active = scriptFilter === tab.id;
            return (
              <button
                key={tab.id}
                type="button"
                role="tab"
                aria-selected={active}
                onClick={() => onScriptFilterChange(tab.id)}
                className={`min-h-[34px] rounded-full px-3.5 py-1 text-xs font-grotesk font-semibold whitespace-nowrap shrink-0 transition-all focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-violet-500 ${
                  active ? 'text-white shadow-sm' : 'hover:opacity-90'
                }`}
                style={
                  active
                    ? { background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)' }
                    : { color: 'var(--text-secondary)' }
                }
              >
                {tab.label}
              </button>
            );
          })}
        </div>
      </div>

      {/* Textarea Input */}
      <div
        className="mt-4 rounded-2xl border p-3.5 transition-colors focus-within:border-violet-500"
        style={{
          backgroundColor: 'var(--surface-elevated)',
          borderColor: 'var(--border-subtle)',
        }}
      >
        <label htmlFor="master-text-input" className="sr-only">
          Type text in English or Hindi
        </label>
        <textarea
          id="master-text-input"
          value={inputText}
          onChange={(e) => onInputChange(e.target.value)}
          placeholder="Type in English or हिंदी..."
          rows={3}
          className="w-full resize-none bg-transparent text-base sm:text-lg font-medium outline-none devanagari-safe placeholder:opacity-60"
          style={{ color: 'var(--text-primary)' }}
        />

        {/* Character Counter & Clear Action */}
        <div
          className="mt-2 flex flex-wrap items-center justify-between gap-2 border-t pt-2.5"
          style={{ borderColor: 'var(--border-subtle)' }}
        >
          <div
            className="flex items-center gap-2 text-xs font-grotesk tabular-nums"
            style={{ color: 'var(--text-secondary)' }}
          >
            <Languages className="h-3.5 w-3.5" style={{ color: 'var(--accent-gold)' }} aria-hidden="true" />
            <span>{hasHindi ? 'Devanagari Detected' : 'Latin Script'}</span>
            <span aria-hidden="true">·</span>
            <span>{charCount} chars</span>
            <span aria-hidden="true">·</span>
            <span>{wordCount} words</span>
          </div>

          {inputText.length > 0 && (
            <button
              type="button"
              onClick={() => onInputChange('')}
              className="inline-flex min-h-[34px] items-center gap-1.5 rounded-full border px-3 py-1 text-xs font-grotesk font-semibold whitespace-nowrap transition-colors hover:border-violet-500 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-violet-500"
              style={{
                backgroundColor: 'var(--surface-card)',
                borderColor: 'var(--border-subtle)',
                color: 'var(--text-secondary)',
              }}
            >
              <Eraser className="h-3.5 w-3.5" aria-hidden="true" />
              Clear
            </button>
          )}
        </div>
      </div>

      {/* Quick Sample Prompts */}
      <div className="mt-3.5">
        <div className="text-xs font-grotesk font-medium mb-2" style={{ color: 'var(--text-muted)' }}>
          Quick Presets
        </div>
        <div className="flex flex-wrap gap-2">
          {SAMPLE_PROMPTS.map((sample) => (
            <button
              key={sample.label}
              type="button"
              onClick={() => {
                onInputChange(sample.text);
                onScriptFilterChange(sample.script);
              }}
              className="min-h-[36px] rounded-full border px-3 py-1 text-xs font-medium whitespace-nowrap transition-colors hover:border-violet-500 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-violet-500"
              style={{
                backgroundColor: 'var(--surface-low)',
                borderColor: 'var(--border-subtle)',
                color: 'var(--text-primary)',
              }}
            >
              {sample.label}
            </button>
          ))}
        </div>
      </div>

      {/* Preview Size Slider */}
      <div
        className="mt-4 flex items-center justify-between gap-3 border-t pt-3.5"
        style={{ borderColor: 'var(--border-subtle)' }}
      >
        <label
          htmlFor="preview-size-slider"
          className="inline-flex items-center gap-1.5 text-xs font-grotesk font-semibold whitespace-nowrap"
          style={{ color: 'var(--text-secondary)' }}
        >
          <Type className="h-3.5 w-3.5" style={{ color: 'var(--accent-violet)' }} aria-hidden="true" />
          Preview Size
        </label>
        <div className="flex flex-1 items-center gap-3 max-w-[200px]">
          <input
            id="preview-size-slider"
            type="range"
            min={16}
            max={34}
            step={1}
            value={previewFontSize}
            onChange={(e) => onPreviewFontSizeChange(Number(e.target.value))}
            className="w-full accent-violet-500 cursor-pointer"
          />
          <span
            className="w-10 text-right font-grotesk text-xs font-semibold tabular-nums"
            style={{ color: 'var(--text-primary)' }}
          >
            {previewFontSize}px
          </span>
        </div>
      </div>
    </section>
  );
};
