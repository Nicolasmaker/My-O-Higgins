import test from 'node:test';
import assert from 'node:assert/strict';
import { getRecommendedQuickAccess, updateQuickAccessUsage } from './quickAccessUtils.js';

const initialItems = [
  { id: 1, title: 'Calendario', clicks: 0 },
  { id: 2, title: 'Portal', clicks: 0 },
  { id: 3, title: 'Certificados', clicks: 0 },
];

test('ordena los accesos por uso y limita la vista a tres', () => {
  const updated = updateQuickAccessUsage(initialItems, 2);
  const recommended = getRecommendedQuickAccess(updated);

  assert.equal(recommended[0].title, 'Portal');
  assert.equal(recommended.length, 3);
});

test('mantiene el orden por defecto cuando no hay uso registrado', () => {
  const recommended = getRecommendedQuickAccess(initialItems);

  assert.deepEqual(recommended.map((item) => item.id), [1, 2, 3]);
});
