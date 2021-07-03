function Text() {
	output("test") ;
	return ;
}


async function main() {
	output("doing...") ;
	await sleep(3000);
	output("ok") ;
	await Text();
	return ;
}

