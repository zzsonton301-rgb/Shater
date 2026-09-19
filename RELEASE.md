# بناء إصدار Android من GitHub Actions

يتم تشغيل Workflow باسم **Android Release** عند دفع Tag يبدأ بـ `v`، أو يدوياً من تبويب Actions عبر `workflow_dispatch`. يقوم الـ Workflow بتثبيت JDK 17 وAndroid SDK وGradle، ثم يبني نسخة Release موقعة وينتج ملفي APK وAAB، ويتحقق من توقيع APK، ويرفعهما كـ Artifacts وينشئ GitHub Release مع ملف SHA-256.

## أسرار GitHub المطلوبة

من إعدادات المستودع افتح **Settings → Secrets and variables → Actions → New repository secret** وأضف الأسرار التالية:

| الاسم | القيمة |
|---|---|
| `ANDROID_KEYSTORE_BASE64` | محتوى ملف `release-keystore.jks` بعد تحويله إلى Base64 بدون أسطر إضافية |
| `ANDROID_KEYSTORE_PASSWORD` | كلمة مرور ملف التوقيع |
| `ANDROID_KEY_ALIAS` | اسم المفتاح داخل ملف التوقيع |
| `ANDROID_KEY_PASSWORD` | كلمة مرور المفتاح |

أنشئ ملف التوقيع مرة واحدة على جهاز آمن بالأمر التالي، مع تغيير القيم الخاصة بك:

```bash
keytool -genkeypair -v -storetype PKCS12 -keystore release-keystore.jks \
  -alias shater-release -keyalg RSA -keysize 2048 -validity 10000
base64 -w 0 release-keystore.jks > release-keystore.base64.txt
```

لا ترفع ملف `release-keystore.jks` أو ملف Base64 إلى GitHub. خزّن القيمة الناتجة في السر `ANDROID_KEYSTORE_BASE64` فقط، واحتفظ بنسخة احتياطية مشفرة من ملف التوقيع وكلمات المرور. فقدان ملف التوقيع يمنع نشر تحديثات موقعة بنفس هوية التطبيق.

## تشغيل البناء

لإنشاء إصدار، ادفع Tag مثل:

```bash
git tag v1.0.0
git push origin v1.0.0
```

أو شغّل Workflow يدوياً واكتب قيمة `release_tag`. بعد النجاح ستجد APK وAAB في صفحة **Actions → Workflow run → Artifacts**، وفي صفحة **Releases** أيضاً.

## ملاحظة أمنية

ملف التوقيع لا يتم رفعه كـ Artifact ولا يُحفظ داخل المستودع. يتم فكّه مؤقتاً على Runner أثناء البناء ثم يُحذف مع انتهاء بيئة GitHub Actions.
