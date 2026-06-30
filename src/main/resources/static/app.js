const state = {
  step: 1,
  totalSteps: 4,
  bodyType: 'ANY',
  fuelType: 'ANY',
  seatingNeeded: 5,
  primaryUse: 'city',
  shortlist: [],
  recommendations: [],
};

const $ = (sel) => document.querySelector(sel);
const $$ = (sel) => document.querySelectorAll(sel);

// --- Init ---
document.addEventListener('DOMContentLoaded', () => {
  bindWizard();
  bindShortlist();
  loadShortlist();
});

function bindWizard() {
  const minBudget = $('#minBudget');
  const maxBudget = $('#maxBudget');

  minBudget.addEventListener('input', () => {
    if (+minBudget.value > +maxBudget.value) maxBudget.value = minBudget.value;
    updateBudgetLabels();
  });
  maxBudget.addEventListener('input', () => {
    if (+maxBudget.value < +minBudget.value) minBudget.value = maxBudget.value;
    updateBudgetLabels();
  });

  ['fuelWeight', 'safetyWeight', 'perfWeight', 'valueWeight'].forEach(id => {
    $(`#${id}`).addEventListener('input', (e) => {
      $(`#${id}Val`).textContent = e.target.value;
    });
  });

  setupChips('#bodyTypeChips', (v) => { state.bodyType = v; });
  setupChips('#fuelTypeChips', (v) => { state.fuelType = v; });
  setupChips('#seatingChips', (v) => { state.seatingNeeded = +v; });

  $$('.use-card').forEach(card => {
    card.addEventListener('click', () => {
      $$('.use-card').forEach(c => c.classList.remove('selected'));
      card.classList.add('selected');
      state.primaryUse = card.dataset.value;
    });
  });

  $('#prevBtn').addEventListener('click', () => goStep(state.step - 1));
  $('#nextBtn').addEventListener('click', () => {
    if (state.step < state.totalSteps) goStep(state.step + 1);
    else submitPreferences();
  });
  $('#backToWizard').addEventListener('click', showWizard);

  updateBudgetLabels();
  updateProgress();
}

function setupChips(containerSel, onSelect) {
  $$(`${containerSel} .chip`).forEach(chip => {
    chip.addEventListener('click', () => {
      $$(`${containerSel} .chip`).forEach(c => c.classList.remove('selected'));
      chip.classList.add('selected');
      onSelect(chip.dataset.value);
    });
  });
}

function updateBudgetLabels() {
  $('#minBudgetLabel').textContent = $('#minBudget').value;
  $('#maxBudgetLabel').textContent = $('#maxBudget').value;
}

function goStep(n) {
  state.step = n;
  $$('.step').forEach(s => s.classList.toggle('active', +s.dataset.step === n));
  $('#prevBtn').disabled = n === 1;
  $('#nextBtn').textContent = n === state.totalSteps ? 'Find my cars →' : 'Next';
  updateProgress();
}

function updateProgress() {
  const pct = (state.step / state.totalSteps) * 100;
  $('#progressFill').style.width = `${pct}%`;
  $('#stepLabel').textContent = `Step ${state.step} of ${state.totalSteps}`;
}

function getPreferences() {
  return {
    minBudgetLakhs: +$('#minBudget').value,
    maxBudgetLakhs: +$('#maxBudget').value,
    fuelType: state.fuelType,
    bodyType: state.bodyType,
    seatingNeeded: state.seatingNeeded,
    fuelEconomyWeight: +$('#fuelWeight').value,
    safetyWeight: +$('#safetyWeight').value,
    performanceWeight: +$('#perfWeight').value,
    valueWeight: +$('#valueWeight').value,
    primaryUse: state.primaryUse,
  };
}

async function submitPreferences() {
  const btn = $('#nextBtn');
  btn.textContent = 'Finding matches...';
  btn.disabled = true;

  try {
    const res = await fetch('/api/recommendations', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(getPreferences()),
    });
    const data = await res.json();
    state.recommendations = data.recommendations;
    showResults(data);
  } catch (e) {
    alert('Something went wrong. Is the server running?');
  } finally {
    btn.textContent = 'Find my cars →';
    btn.disabled = false;
  }
}

function showResults(data) {
  $('#wizard').classList.add('hidden');
  $('#results').classList.remove('hidden');
  $('#resultsSummary').textContent = data.summary;

  const grid = $('#resultsGrid');
  grid.innerHTML = '';

  if (!data.recommendations.length) {
    grid.innerHTML = '<p style="color:var(--text-muted);text-align:center;padding:2rem">No matches found. Try widening your budget or selecting "Any" for body/fuel type.</p>';
    return;
  }

  data.recommendations.forEach((car, i) => {
    const inShortlist = state.shortlist.some(s => s.id === car.id);
    const card = document.createElement('div');
    card.className = `car-card${i === 0 ? ' top-pick' : ''}`;
    card.innerHTML = `
      <div>
        ${i === 0 ? '<div class="car-rank">🏆 Top pick</div>' : `<div class="car-rank">#${i + 1}</div>`}
        <div class="car-name">${car.displayName}</div>
        <div class="car-meta">${car.highlights}</div>
        <div class="specs">
          <span class="spec">₹${car.priceInLakhs}L</span>
          <span class="spec">${car.fuelType}</span>
          <span class="spec">${car.bodyType}</span>
          ${car.mileageKmpl > 0 ? `<span class="spec">${car.mileageKmpl} kmpl</span>` : '<span class="spec">Electric</span>'}
          <span class="spec">★ ${car.userRating} (${car.reviewCount})</span>
          <span class="spec">Safety ${car.safetyRating}/5</span>
        </div>
        <ul class="reasons">${car.matchReasons.map(r => `<li>${r}</li>`).join('')}</ul>
      </div>
      <div class="match-score">
        <div class="score-circle" style="--pct: ${Math.min(car.matchScore, 100)}">
          <span>${Math.round(car.matchScore)}</span>
        </div>
        <div class="match-label">match %</div>
      </div>
      <div class="car-actions">
        <button class="btn-sm ${inShortlist ? 'added' : ''}" data-car-id="${car.id}">
          ${inShortlist ? '✓ In shortlist' : '+ Add to shortlist'}
        </button>
      </div>
    `;
    card.querySelector('.btn-sm').addEventListener('click', (e) => toggleShortlist(car, e.target));
    grid.appendChild(card);
  });
}

function showWizard() {
  $('#results').classList.add('hidden');
  $('#wizard').classList.remove('hidden');
}

// --- Shortlist ---
function bindShortlist() {
  $('#shortlistToggle').addEventListener('click', () => {
    $('#shortlistDrawer').classList.remove('hidden');
    renderShortlistDrawer();
  });
  $('#closeDrawer').addEventListener('click', closeDrawer);
  $('#drawerBackdrop').addEventListener('click', closeDrawer);
}

function closeDrawer() {
  $('#shortlistDrawer').classList.add('hidden');
}

async function loadShortlist() {
  try {
    const res = await fetch('/api/shortlist');
    const data = await res.json();
    state.shortlist = data.cars;
    updateShortlistBadge();
  } catch (e) { /* server not up yet */ }
}

async function toggleShortlist(car, btn) {
  const inList = state.shortlist.some(s => s.id === car.id);
  try {
    const res = await fetch(`/api/shortlist/${car.id}`, {
      method: inList ? 'DELETE' : 'POST',
    });
    const data = await res.json();
    if (!res.ok) {
      alert(data.message || 'Could not update shortlist');
      return;
    }
    state.shortlist = data.cars;
    updateShortlistBadge();
    if (btn) {
      btn.textContent = inList ? '+ Add to shortlist' : '✓ In shortlist';
      btn.classList.toggle('added', !inList);
    }
    renderShortlistDrawer();
  } catch (e) {
    alert('Could not update shortlist');
  }
}

function updateShortlistBadge() {
  $('#shortlistCount').textContent = state.shortlist.length;
}

function renderShortlistDrawer() {
  const content = $('#shortlistContent');
  const compare = $('#compareTable');

  if (!state.shortlist.length) {
    content.innerHTML = '<div class="empty-shortlist">No cars yet. Add some from your recommendations!</div>';
    compare.classList.add('hidden');
    return;
  }

  content.innerHTML = state.shortlist.map(car => `
    <div class="shortlist-item">
      <div>
        <div class="name">${car.displayName}</div>
        <div class="price">₹${car.priceInLakhs}L · ${car.fuelType} · ★ ${car.userRating}</div>
      </div>
      <button class="remove-btn" data-id="${car.id}">Remove</button>
    </div>
  `).join('');

  content.querySelectorAll('.remove-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const car = state.shortlist.find(c => c.id === +btn.dataset.id);
      if (car) toggleShortlist(car);
    });
  });

  if (state.shortlist.length >= 2) {
    compare.classList.remove('hidden');
    const specs = ['priceInLakhs', 'mileageKmpl', 'safetyRating', 'horsepower', 'userRating', 'fuelType', 'bodyType'];
    const labels = { priceInLakhs: 'Price (₹L)', mileageKmpl: 'Mileage', safetyRating: 'Safety', horsepower: 'Power (bhp)', userRating: 'Rating', fuelType: 'Fuel', bodyType: 'Body' };

    compare.innerHTML = `<table>
      <thead><tr><th>Spec</th>${state.shortlist.map(c => `<th>${c.make} ${c.model}</th>`).join('')}</tr></thead>
      <tbody>${specs.map(s => `
        <tr><td>${labels[s]}</td>${state.shortlist.map(c => `<td>${c[s]}${s === 'priceInLakhs' ? 'L' : s === 'mileageKmpl' && c[s] === 0 ? ' (EV)' : ''}</td>`).join('')}</tr>
      `).join('')}</tbody>
    </table>`;
  } else {
    compare.classList.add('hidden');
  }
}
