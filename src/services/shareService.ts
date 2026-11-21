export async function copyToClipboard(text: string): Promise<boolean> {
  if (!text) return false;
  try {
    if (navigator.clipboard && navigator.clipboard.writeText) {
      await navigator.clipboard.writeText(text);
      return true;
    }
  } catch {
    // Fall through to legacy textarea fallback
  }

  try {
    const textarea = document.createElement('textarea');
    textarea.value = text;
    textarea.setAttribute('readonly', '');
    textarea.style.position = 'fixed';
    textarea.style.opacity = '0';
    textarea.style.pointerEvents = 'none';
    document.body.appendChild(textarea);
    textarea.select();
    const ok = document.execCommand('copy');
    document.body.removeChild(textarea);
    return ok;
  } catch {
    return false;
  }
}

export function isWebShareSupported(): boolean {
  return typeof navigator !== 'undefined' && typeof navigator.share === 'function';
}

export async function nativeShareText(title: string, text: string): Promise<'shared' | 'unsupported' | 'cancelled'> {
  if (!isWebShareSupported()) return 'unsupported';
  try {
    await navigator.share({ title, text });
    return 'shared';
  } catch (err) {
    if (err instanceof Error && err.name === 'AbortError') {
      return 'cancelled';
    }
    return 'unsupported';
  }
}

export async function nativeSharePng(
  blob: Blob,
  fileName: string,
  title: string,
  text: string
): Promise<'shared' | 'unsupported' | 'cancelled'> {
  if (!isWebShareSupported()) return 'unsupported';
  try {
    const file = new File([blob], fileName, { type: 'image/png' });
    const nav = navigator as Navigator & { canShare?: (data?: ShareData) => boolean };
    if (nav.canShare && !nav.canShare({ files: [file] })) {
      return 'unsupported';
    }
    await navigator.share({
      title,
      text,
      files: [file],
    });
    return 'shared';
  } catch (err) {
    if (err instanceof Error && err.name === 'AbortError') {
      return 'cancelled';
    }
    return 'unsupported';
  }
}

export function buildPlatformShareUrls(text: string) {
  const encoded = encodeURIComponent(text);
  return {
    whatsapp: `https://wa.me/?text=${encoded}`,
    telegram: `https://t.me/share/url?url=${encodeURIComponent(' ')}&text=${encoded}`,
    facebook: `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent('https://helpboldigraphi.app')}&quote=${encoded}`,
    instagram: 'https://www.instagram.com/',
  };
}
