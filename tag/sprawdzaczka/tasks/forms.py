from django import forms
from .models import Submission

class SubmissionForm(forms.ModelForm):
    class Meta:
        model = Submission
        fields = ["code"]

    def clean_code(self):
        code = self.cleaned_data["code"]

        if len(code) > 10_000:
            raise forms.ValidationError("Za długi kod.")

        return code