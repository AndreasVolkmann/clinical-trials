select distinct bs.id
from brief_summaries bs
inner join keywords k on k.nct_id = bs.nct_id
left join studies s on s.nct_id = bs.nct_id
left join conditions c on c.nct_id = bs.nct_id
left join interventions i on i.nct_id = bs.nct_id
left join design_groups dg on dg.nct_id = bs.nct_id
left join design_outcomes outcome on outcome.nct_id = bs.nct_id
left join sponsors spo on spo.nct_id = bs.nct_id
where k.name in ('Cancer', 'Depression');