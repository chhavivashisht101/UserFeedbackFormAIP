(function () {
  'use strict';

  /* ---------- confetti on the servlet success page ---------- */
  var box = document.getElementById('confetti');
  if (box && box.dataset.fire === 'true') {
    var colors = ['#6366f1', '#8b5cf6', '#ec4899', '#fbbf24', '#22c55e', '#38bdf8'];
    for (var i = 0; i < 70; i++) {
      var p = document.createElement('span');
      p.className = 'piece';
      p.style.left = Math.random() * 100 + 'vw';
      p.style.background = colors[i % colors.length];
      p.style.animationDuration = (2.4 + Math.random() * 2.6) + 's';
      p.style.animationDelay = (Math.random() * 1.2) + 's';
      p.style.borderRadius = Math.random() > .5 ? '50%' : '2px';
      box.appendChild(p);
    }
    setTimeout(function () { box.innerHTML = ''; }, 8000);
  }

  /* ---------- form page ---------- */
  var form = document.getElementById('feedbackForm');
  if (!form) return;

  var switchBox = document.querySelector('.method-switch');
  var btns = document.querySelectorAll('.m-btn');
  var hint = document.getElementById('methodHint');
  var btnText = document.querySelector('.btn-text');
  var hints = {
    post: 'POST sends data in the request body (recommended).',
    get: 'GET puts the data in the URL. Do not use it for private information.'
  };

  btns.forEach(function (b) {
    b.addEventListener('click', function () {
      var m = b.dataset.method;
      form.method = m;
      btns.forEach(function (x) { x.classList.toggle('active', x === b); });
      switchBox.classList.toggle('get', m === 'get');
      hint.textContent = hints[m];
      btnText.textContent = 'Submit using ' + m.toUpperCase();
    });
  });

  /* character counter */
  var ta = document.getElementById('feedback');
  var count = document.getElementById('count');
  ta.addEventListener('input', function () { count.textContent = ta.value.length; });

  /* rating label */
  var words = { 1: 'Poor', 2: 'Fair', 3: 'Good', 4: 'Very good', 5: 'Excellent' };
  var ratingText = document.getElementById('ratingText');
  document.querySelectorAll('input[name=rating]').forEach(function (r) {
    r.addEventListener('change', function () {
      ratingText.textContent = words[r.value];
      document.querySelector('.rating-block').classList.remove('invalid');
    });
  });

  /* validation */
  var emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
  function check(el, ok) { el.closest('.field').classList.toggle('invalid', !ok); return ok; }

  ['name', 'email', 'feedback'].forEach(function (id) {
    document.getElementById(id).addEventListener('input', function () {
      this.closest('.field').classList.remove('invalid');
    });
  });

  form.addEventListener('submit', function (e) {
    var n = document.getElementById('name');
    var em = document.getElementById('email');
    var fb = document.getElementById('feedback');
    var okName = check(n, n.value.trim() !== '');
    var okMail = check(em, emailRe.test(em.value.trim()));
    var okFb = check(fb, fb.value.trim() !== '');
    var okRate = !!form.querySelector('input[name=rating]:checked');
    document.querySelector('.rating-block').classList.toggle('invalid', !okRate);

    if (!(okName && okMail && okFb && okRate)) { e.preventDefault(); return; }
    document.getElementById('submitBtn').classList.add('loading');
  });
})();
