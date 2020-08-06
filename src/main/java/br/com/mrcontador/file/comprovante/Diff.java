package br.com.mrcontador.file.comprovante;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

public class Diff {

	public static void twopatch(String original, String revision) throws DiffException {
		DiffText d = new DiffText();
		Map<Integer, DiffValue> map = d.doDiff(original, revision);
		List<DiffValue> list = new ArrayList<DiffValue>();
		for (DiffValue _values : map.values()) {
			diffpatch(list, _values.getOldValue(), _values.getNewValue());
		}
	}

	public static void diffpatch(List<DiffValue> list, String original, String revision) throws DiffException {
		Patch<String> patchs = DiffUtils.diffInline(original, revision);
		List<AbstractDelta<String>> deltas = patchs.getDeltas();
		for (AbstractDelta<String> delta : deltas) {
			if (delta.getType().equals(DeltaType.CHANGE)) {
				DiffValue diffValues = new DiffValue();
				diffValues.setOldValue(delta.getSource().getLines().get(0));
				diffValues.setNewValue(delta.getTarget().getLines().get(0));
				list.add(diffValues);
			}
			if (delta.getType().equals(DeltaType.INSERT)) {
				list.get(list.size() - 1).setNewValue(list.get(list.size() - 1).getNewValue().concat(" ")
						.concat(delta.getTarget().getLines().get(0)));
			}

		}
	}

	public static void patch(String original, String revision) throws DiffException {
		DiffRowGenerator generator = DiffRowGenerator.create().showInlineDiffs(true).inlineDiffByWord(true)
				.oldTag(f -> "~").newTag(f -> "**").build();
		List<DiffRow> rows = generator.generateDiffRows(Arrays.asList(original.split("\\r?\\n")),
				Arrays.asList(revision.split("\\r?\\n")));
		for (DiffRow row : rows) {
			System.out.println("|" + row.getOldLine() + "|" + row.getNewLine() + "|");
		}
	}
}
