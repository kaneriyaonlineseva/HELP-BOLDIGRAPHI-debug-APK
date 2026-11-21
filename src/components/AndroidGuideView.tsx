import React, { useState } from 'react';
import { Check, Copy, FolderTree, Smartphone, Terminal } from 'lucide-react';
import { copyToClipboard } from '../services/shareService';

export const AndroidGuideView: React.FC = () => {
  const [copiedCmd, setCopiedCmd] = useState<string | null>(null);

  const handleCopy = async (id: string, text: string) => {
    const ok = await copyToClipboard(text);
    if (ok) {
      setCopiedCmd(id);
      setTimeout(() => setCopiedCmd(null), 1600);
    }
  };

  const buildCmds = [
    {
      id: 'test',
      label: 'Run Unit Tests',
      command: 'cd android && ./gradlew testDebugUnitTest',
    },
    {
      id: 'debug-apk',
      label: 'Build Debug APK',
      command: 'cd android && ./gradlew assembleDebug',
    },
    {
      id: 'release-apk',
      label: 'Build Release APK',
      command: 'cd android && ./gradlew assembleRelease',
    },
    {
      id: 'install',
      label: 'Install on Connected Device (ADB)',
      command: 'adb install -r android/app/build/outputs/apk/debug/app-debug.apk',
    },
  ];

  return (
    <section aria-label="Native Android Kotlin Project Guide" className="space-y-6">
      <div
        className="rounded-3xl border p-6"
        style={{
          backgroundColor: 'var(--surface-card)',
          borderColor: 'var(--border-subtle)',
        }}
      >
        <div className="flex items-start gap-3">
          <div
            className="flex h-11 w-11 shrink-0 items-center justify-center rounded-2xl"
            style={{ background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)' }}
          >
            <Smartphone className="h-5 w-5 text-white" aria-hidden="true" />
          </div>
          <div>
            <h1 className="font-syne text-2xl font-extrabold" style={{ color: 'var(--text-primary)' }}>
              Native Android (Kotlin + Jetpack Compose)
            </h1>
            <p className="mt-1 text-sm leading-relaxed" style={{ color: 'var(--text-secondary)' }}>
              A complete native Android Studio project is included in the <code className="font-grotesk font-semibold">/android</code> directory, matching the Obsidian Luxury Typographer Material 3 design system, bilingual Unicode + Devanagari engine, native Share Intents, FileProvider PNG export, and DataStore persistence.
            </p>
          </div>
        </div>
      </div>

      {/* Architecture Highlights */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div
          className="rounded-2xl border p-5"
          style={{
            backgroundColor: 'var(--surface-card)',
            borderColor: 'var(--border-subtle)',
          }}
        >
          <div className="flex items-center gap-2 mb-3">
            <FolderTree className="h-4 w-4" style={{ color: 'var(--accent-gold)' }} aria-hidden="true" />
            <h2 className="font-syne text-base font-bold" style={{ color: 'var(--text-primary)' }}>
              Project Modules & Layers
            </h2>
          </div>
          <ul className="space-y-2 text-sm" style={{ color: 'var(--text-secondary)' }}>
            <li><strong style={{ color: 'var(--text-primary)' }}>domain/engine:</strong> Pure Kotlin <code className="font-grotesk text-xs">UnicodeStylizerEngine</code>, <code className="font-grotesk text-xs">DevanagariCatalog</code> &amp; <code className="font-grotesk text-xs">SocialTemplatesCatalog</code></li>
            <li><strong style={{ color: 'var(--text-primary)' }}>res/font:</strong> Bundled SIL OFL 1.1 TTF fonts (<code className="font-grotesk text-xs">Mukta</code>, <code className="font-grotesk text-xs">Hind</code>, <code className="font-grotesk text-xs">Noto Sans Devanagari</code>, <code className="font-grotesk text-xs">Tiro Devanagari Hindi</code>, <code className="font-grotesk text-xs">Rozha One</code>)</li>
            <li><strong style={{ color: 'var(--text-primary)' }}>data &amp; platform:</strong> DataStore persistence + <code className="font-grotesk text-xs">AndroidShareHelper</code> (Clipboard, WhatsApp/FB/IG/TG, MediaStore PNG &amp; FileProvider)</li>
            <li><strong style={{ color: 'var(--text-primary)' }}>ui:</strong> 5-tab Compose Material 3 (Studio, Templates, Favorites, History, Settings) with English &amp; हिन्दी UI</li>
          </ul>
        </div>

        <div
          className="rounded-2xl border p-5"
          style={{
            backgroundColor: 'var(--surface-card)',
            borderColor: 'var(--border-subtle)',
          }}
        >
          <div className="flex items-center gap-2 mb-3">
            <Terminal className="h-4 w-4" style={{ color: 'var(--accent-violet)' }} aria-hidden="true" />
            <h2 className="font-syne text-base font-bold" style={{ color: 'var(--text-primary)' }}>
              Testing &amp; APK Commands
            </h2>
          </div>
          <div className="space-y-2.5">
            {buildCmds.map((item) => {
              const isCopied = copiedCmd === item.id;
              return (
                <div
                  key={item.id}
                  className="flex items-center justify-between gap-2 rounded-xl border px-3 py-2"
                  style={{
                    backgroundColor: 'var(--surface-low)',
                    borderColor: 'var(--border-subtle)',
                  }}
                >
                  <div className="min-w-0">
                    <div className="text-[11px] font-grotesk font-semibold" style={{ color: 'var(--text-muted)' }}>
                      {item.label}
                    </div>
                    <code className="block truncate text-xs font-mono" style={{ color: 'var(--text-primary)' }}>
                      {item.command}
                    </code>
                  </div>
                  <button
                    type="button"
                    onClick={() => handleCopy(item.id, item.command)}
                    aria-label={`Copy ${item.label}`}
                    className="inline-flex min-h-[34px] min-w-[34px] items-center justify-center rounded-lg border shrink-0"
                    style={{
                      backgroundColor: 'var(--surface-card)',
                      borderColor: isCopied ? 'var(--accent-gold)' : 'var(--border-subtle)',
                      color: isCopied ? 'var(--accent-gold)' : 'var(--text-primary)',
                    }}
                  >
                    {isCopied ? <Check className="h-3.5 w-3.5" /> : <Copy className="h-3.5 w-3.5" />}
                  </button>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </section>
  );
};
