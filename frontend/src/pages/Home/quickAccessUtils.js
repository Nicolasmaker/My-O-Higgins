const QUICK_ACCESS_STORAGE_KEY = 'myohiggins.quick-access-usage';
const MAX_RECOMMENDED_ITEMS = 3;

function readUsageStorage() {
  if (typeof window === 'undefined') return {};

  try {
    const raw = window.localStorage.getItem(QUICK_ACCESS_STORAGE_KEY);
    if (!raw) return {};
    return JSON.parse(raw);
  } catch (error) {
    console.warn('No se pudo leer el historial de accesos rápidos.', error);
    return {};
  }
}

function writeUsageStorage(usageMap) {
  if (typeof window === 'undefined') return;

  window.localStorage.setItem(QUICK_ACCESS_STORAGE_KEY, JSON.stringify(usageMap));
}

export function initializeQuickAccessItems(items) {
  const usageMap = readUsageStorage();
  return items.map((item) => ({
    ...item,
    clicks: usageMap[item.id] || 0,
  }));
}

export function updateQuickAccessUsage(items, itemId) {
  const usageMap = readUsageStorage();
  const updatedItems = items.map((item) => ({ ...item, clicks: usageMap[item.id] || 0 }));

  const target = updatedItems.find((item) => item.id === itemId);
  if (!target) return updatedItems;

  const nextUsage = {
    ...usageMap,
    [itemId]: (usageMap[itemId] || 0) + 1,
  };

  writeUsageStorage(nextUsage);

  return updatedItems
    .map((item) => ({
      ...item,
      clicks: item.id === itemId ? (item.clicks || 0) + 1 : item.clicks || 0,
    }))
    .sort((a, b) => (b.clicks || 0) - (a.clicks || 0));
}

export function getRecommendedQuickAccess(items) {
  const sortedItems = [...items].sort((a, b) => (b.clicks || 0) - (a.clicks || 0));
  return sortedItems.slice(0, MAX_RECOMMENDED_ITEMS);
}
